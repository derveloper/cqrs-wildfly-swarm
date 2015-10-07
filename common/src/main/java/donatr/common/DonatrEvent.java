package donatr.common;

import com.google.gson.Gson;
import com.zanox.rabbiteasy.cdi.ContainsContent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DonatrEvent<T> implements ContainsContent<String> {
	private T payload;

	@Override
	public String getContent() {
		String json = new Gson().toJson(payload);
		System.out.println(json);
		return json;
	}

	@Override
	public String toString() {
		return getContent();
	}
}
