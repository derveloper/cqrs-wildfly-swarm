package donatr.domain.account.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import donatr.domain.account.aggregate.AccountType;

import javax.annotation.PostConstruct;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Converter(autoApply = true)
@Provider
public class AccountTypeConverter implements AttributeConverter<AccountType, String>, ContextResolver<ObjectMapper>
{
	@Override
	public String convertToDatabaseColumn(AccountType type) {
		return type.getValue();
	}

	@Override
	public AccountType convertToEntityAttribute(String type) {
		return AccountType.parse(type);
	}

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

	@PostConstruct
	public void customConfiguration(){
		mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
	}
}
