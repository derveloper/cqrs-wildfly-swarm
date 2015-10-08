package donatr.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zanox.rabbiteasy.cdi.ContainsContent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
public abstract class DonatrEvent implements ContainsContent<String> {
	private static final String EVENT_NAME_FIELD_NAME    = "event_name";
	private static final String EVENT_PAYLOAD_FIELD_NAME = "event_payload";
	private Object payload;

	@Override
	public void setContent(String content) {
		System.out.println(content);
		String event_payload = new Gson().fromJson(content, JsonObject.class)
				.getAsJsonObject(EVENT_PAYLOAD_FIELD_NAME).toString();
		payload = new Gson().fromJson(event_payload, this.getClass());
		try {
			BeanUtils.copyProperties(this, payload);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getContent() {
		String toString = toString();
		System.out.println(toString);
		return toString;
	}

	@Override
	public String toString() {
		String jsonPojo = new Gson().toJson(this);
		System.out.println(jsonPojo);

		try {
			StringWriter stringWriter = new StringWriter();
			new Gson().newJsonWriter(stringWriter)
					.beginObject()
					.name(EVENT_NAME_FIELD_NAME).value(this.getClass().getSimpleName())
					.name(EVENT_PAYLOAD_FIELD_NAME).jsonValue(jsonPojo)
					.endObject().close();
			String jsonEvent = stringWriter.toString();
			stringWriter.close();

			return jsonEvent;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this.getClass().getSimpleName() + jsonPojo;
	}
}
