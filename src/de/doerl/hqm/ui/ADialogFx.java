package de.doerl.hqm.ui;

import de.doerl.hqm.utils.ResourceManager;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class ADialogFx extends Stage {
	public static final String BTN_OK = "btn.ok";
	public static final String BTN_CANCEL = "btn.cancel";
	public static final String BTN_YES = "btn.yes";
	public static final String BTN_NO = "btn.no";
	public static final String BTN_NEW = "btn.new";
	public static final String BTN_EDIT = "btn.edit";
	public static final String BTN_DELETE = "btn.delete";
	public static final int GAP = 10;
	protected HBox mSelect = new HBox( GAP);
	protected VBox mMain = new VBox( GAP);

	protected ADialogFx( Stage owner) {
		initOwner( owner);
		initStyle( StageStyle.UTILITY);
		initModality( Modality.APPLICATION_MODAL);
		mSelect.setAlignment( Pos.CENTER_RIGHT);
		mMain.setAlignment( Pos.TOP_LEFT);
		VBox content = new VBox( GAP);
		content.setPadding( new Insets( GAP, GAP, GAP, GAP));
		content.getChildren().add( mMain);
		content.getChildren().add( mSelect);
		setScene( new Scene( content));
	}

	protected void addAction( String name) {
		Button btnOk = new Button( name);
		btnOk.setAlignment( Pos.CENTER_RIGHT);
		btnOk.setOnAction( event -> close());
		mSelect.getChildren().add( btnOk);
	}

	protected void addEscapeAction() {
		onKeyAction( key -> {
			if (key.getCode() == KeyCode.ESCAPE) {
				close();
			}
		});
	}

	protected abstract void createMain();

	protected void onKeyAction( EventHandler<KeyEvent> a) {
		getScene().setOnKeyPressed( a);
	}

	public void setTheme( String theme) {
		if (theme != null) {
			setTitle( ResourceManager.getString( theme));
		}
	}

	protected void showDialog() {
		sizeToScene();
		centerOnScreen();
		showAndWait();
	}
}
