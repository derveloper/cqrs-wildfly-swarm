package donatr.eventstore;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String event;
}
