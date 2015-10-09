package donatr.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zanox.rabbiteasy.cdi.ContainsContent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Getter
@Setter
public abstract class DonatrEvent implements ContainsContent<String> {
	private static final String EVENT_NAME_FIELD_NAME    = "event_name";
	private static final String EVENT_PAYLOAD_FIELD_NAME = "event_payload";

	@Override
	public void setContent(String content) {
		System.out.println(content);
		String event_payload = new Gson().fromJson(content, JsonObject.class)
				.getAsJsonObject(EVENT_PAYLOAD_FIELD_NAME).toString();
		Object payload = new Gson().fromJson(event_payload, this.getClass());
		Mapper mapper = new DozerBeanMapper();
		mapper.map(payload, this.getClass());
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
