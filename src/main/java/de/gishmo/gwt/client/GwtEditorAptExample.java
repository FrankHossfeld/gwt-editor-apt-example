package de.gishmo.gwt.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

import de.gishmo.gwt.editor.client.annotation.IsEditor;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
@IsEditor(Person.class)
public class GwtEditorAptExample
  implements EntryPoint,
             Editor<Person> {

  TextBox name;

  @Path("firstName")
  TextBox firstname;

  @Path("address.street")
  TextBox street;

  @Path("date")
  DateBox date;

  private DialogBox dialogBox = new DialogBox();
  private HTML    serverResponseLabel = new HTML();



  private SimpleBeanEditorDriver<Person, GwtEditorAptExample> driver = new GwtEditorAptExampleEditorDriverImpl();

  private Person person = new Person("Simpson",
                                     "Bart",
                                     new Date(),
                                     new Address("abs",
                                                 "xyz",
                                                 "Springfield"));

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    VerticalPanel vp = new VerticalPanel();
    RootLayoutPanel.get()
                   .add(vp);

    firstname = new TextBox();
    vp.add(firstname);
    name = new TextBox();
    vp.add(name);
    street = new TextBox();
    vp.add(street);
    date = new DateBox();
    vp.add(date);

    Button flushButton = new Button("flush");
    flushButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        person = driver.flush();

        StringBuilder sb                = new StringBuilder("");
        sb.append("<br><br>")
          .append("first name:")
          .append(person.getFirstName())
          .append("<br>")
          .append("name:")
          .append(person.getName())
          .append("<br>")
          .append("street:")
          .append(person.getAddress().getStreet())
          .append("<br>")
          .append("date:")
          .append(person.getDate().toString())
          .append("<br><br>");

        serverResponseLabel.setHTML(sb.toString());

        dialogBox.center();
      }
    });
    vp.add(flushButton);

    // Create the popup dialog box
    dialogBox.setText("flushed data");
    final Button closeButton = new Button("Close");
    // We can set the id of a widget by accessing its Element
    closeButton.getElement()
               .setId("closeButton");
    final Label  textToServerLabel = new Label("data after flush:");

    VerticalPanel dialogVPanel        = new VerticalPanel();
    dialogVPanel.addStyleName("dialogVPanel");
    dialogVPanel.add(textToServerLabel);
    dialogVPanel.add(serverResponseLabel);
    dialogVPanel.add(closeButton);
    dialogBox.setWidget(dialogVPanel);

    closeButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        dialogBox.hide();
      }
    });

    driver.initialize(this);
    driver.edit(person);
  }
}
