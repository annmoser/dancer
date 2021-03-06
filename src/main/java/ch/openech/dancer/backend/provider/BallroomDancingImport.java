package ch.openech.dancer.backend.provider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.minimalj.repository.query.By;
import org.minimalj.util.CsvReader;

import ch.openech.dancer.backend.DanceEventProvider;
import ch.openech.dancer.backend.EventUpdateCounter;
import ch.openech.dancer.model.DanceEvent;
import ch.openech.dancer.model.EventStatus;
import ch.openech.dancer.model.EventTag;
import ch.openech.dancer.model.Location;
import ch.openech.dancer.model.Region;

public class BallroomDancingImport extends DanceEventProvider {
	private static final long serialVersionUID = 1L;

	@Override
	public EventUpdateCounter updateEvents() {
		EventUpdateCounter result = new EventUpdateCounter();

		CsvReader reader = new CsvReader(getClass().getResourceAsStream("/ch/openech/dancer/data/ballroom_dancing.csv"));
		for (DanceEvent event : reader.readValues(DanceEvent.class)) {
			Optional<DanceEvent> danceEventOptional = findOne(DanceEvent.class,
					By.field(DanceEvent.$.location, location).and(By.field(DanceEvent.$.date, event.date)));

			if (!danceEventOptional.isPresent() && event.date.isAfter(LocalDate.now())) {
				event.location = this.location;
				event.header = "Ballroom Dancing";
				event.title = "Ballroom Dancing";
				event.from = LocalTime.of(20, 00);
				event.until = LocalTime.of(0, 30);
				event.price = BigDecimal.valueOf(15);
				event.status = EventStatus.generated;
				event.tags.add(EventTag.Taxidancer);

				save(event, result);
			}
		}
		return result;
	}

	@Override
	protected Location createLocation() {
		Location location = new Location();
		location.country = "Schweiz";
		location.region.add(Region.LU);
		location.school = false;
		location.address = "Zentralstrasse 7";
		location.city = "5623 Boswil";
		location.name = "Gasthof Löwen (Chillout)";
		location.url = "https://www.ballroomdancingfreiamt.ch/";
		return location;
	}

//	public static void main(String[] args) {
//		LocalDate start = LocalDate.now();
//		LocalDate firstFriday = start.with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY));
//		LocalDate thirdFriday = firstFriday.plusDays(14);
//		if (thirdFriday.isBefore(start)) {
//			start = start.plusMonths(1);
//		}
//
//		for (int i = 0; i < 15; i++) {
//			LocalDate date = start.plusMonths(i).with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY)).plusDays(14);
//			if (date.getMonth() == Month.DECEMBER || date.getMonth() == Month.JULY || date.getMonth() == Month.AUGUST) {
//				continue;
//			}
//
//			System.out.println(date.toString());
//		}
//	}

}
