package ch.openech.dancer.crawler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.minimalj.util.DateUtils;

import ch.openech.dancer.model.DanceEvent;
import ch.openech.dancer.model.DanceEventPeriod;
import ch.openech.dancer.model.Location;
import ch.openech.dancer.model.Organizer;

public class PasadenaCrawler implements DanceEventCrawler {

	private static final String PASADENA_AGENDA_URL = "http://www.pasadena.ch/m/agenda/";

	@Override
	public List<DanceEvent> crawlEvents() {
		List<DanceEvent> danceEvents = new ArrayList<>();
		Document doc;
		try {
			doc = Jsoup.connect(PASADENA_AGENDA_URL).get();
			Elements elements = doc.select("p[style]");
			elements.stream().forEach(element -> {
				
				DanceEvent danceEvent = new DanceEvent();
				danceEvent.start = extractLocalDate(element);
				danceEvent.title = extractDanceEventTitle(element);
				extractPeriod(element, danceEvent.danceEventPeriod);
				danceEvent.description = element.nextElementSibling().text();
				
				danceEvents.add(danceEvent);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return danceEvents;
	}
	
	private void extractPeriod(Element element, DanceEventPeriod danceEventPeriod) {
		if (element.childNodeSize() == 3) {
			String period = element.childNode(2).toString();
			if (period != null) {
				String[] moments = period.split("bis");
				if (moments.length == 2) {
					danceEventPeriod.from = LocalTime.parse(moments[0].trim(), DateUtils.TIME_FORMAT);
					danceEventPeriod.until = LocalTime.parse(moments[1].trim(), DateUtils.TIME_FORMAT);
				}
			}
		}
	}
	
	private LocalDate extractLocalDate(Element element) {
		Element dateTag = element.child(0);
		if (dateTag != null) {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd. MMMM", Locale.GERMAN);
			final int currentYear = LocalDate.now().getYear();
			return MonthDay.parse(dateTag.ownText(), formatter).atYear(currentYear);
		}
		return null;
	}
	
	private String extractDanceEventTitle(Element paragraphElement) {
		Elements elements = paragraphElement.nextElementSibling().getElementsByTag("h3");
		if (elements != null && !elements.isEmpty()) {
			return elements.get(0).ownText();
		}
		return null;
	}
	
	public static Organizer createOrganizer() {
		Organizer organizer = new Organizer();
		organizer.country = "Schweiz";
		organizer.address = "Chriesbaumstrasse 2";
		organizer.city = "8604 Volketswil";
		organizer.name = "Pasadena";
		organizer.url = "http://www.pasadena.ch";
		return organizer;
	}
	
	public static Location createLocation() {
		Location location = new Location();
		location.country = "Schweiz";
		location.address = "Chriesbaumstrasse 2";
		location.city = "8604 Volketswil";
		location.name = "Pasadena";
		location.url = "http://www.pasadena.ch";
		return location;
	}
	

}