package ch.openech.dancer.frontend;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.repository.query.By;

import ch.openech.dancer.model.DanceEvent;
import ch.openech.dancer.model.EventStatus;
import ch.openech.dancer.model.Location;

public class DanceEventLocationTablePage extends SimpleTableEditorPage<DanceEvent> {

	private static final Object[] KEYS = new Object[] { DanceEvent.$.date, DanceEvent.$.title, DanceEvent.$.status };

	private final Location location;

	public DanceEventLocationTablePage(Location location) {
		super(KEYS);
		this.location = location;
	}

	@Override
	public List<Action> getTableActions() {
		return Arrays.asList(new TableNewObjectEditor(), new TableEditor(), new DeleteDetailAction());
	}

	@Override
	protected List<DanceEvent> load() {
		return Backend.find(DanceEvent.class, By.field(DanceEvent.$.location, location).order(DanceEvent.$.date));
	}

	@Override
	protected DanceEvent createObject() {
		DanceEvent event = new DanceEvent();
		event.location = location;
		event.status = EventStatus.published;
		return event;
	}

	@Override
	protected Form<DanceEvent> createForm(boolean editable, boolean newObject) {
		return new DanceEventForm(editable);
	}

	@Override
	protected void validate(DanceEvent event, boolean newObject, List<ValidationMessage> validationMessages) {
		if (newObject) {
			if (event.date != null && event.date.isBefore(LocalDate.now())) {
				validationMessages.add(new ValidationMessage(DanceEvent.$.date, "Muss in Zukunft liegen"));
			}
		}
	}

}