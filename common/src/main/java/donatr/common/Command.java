package donatr.common;

import com.google.gson.Gson;
import com.zanox.rabbiteasy.cdi.ContainsContent;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlTransient;

@Getter
@Setter
public abstract class Command<T> implements ContainsContent<String> {
	@XmlTransient
	private T payload;

	@Override
	public String getContent() {
		String json = new Gson().toJson(payload);
		System.out.println(json);
		return json;
	}
}
