package de.doerl.hqm.ui;

import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.VersionHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

class AboutDialogFx extends Stage {
	protected HBox mSelect = new HBox();

	private AboutDialogFx( Stage owner) {
		initOwner( owner);
		setTheme( "about.theme");
		initStyle( StageStyle.UTILITY);
		initModality( Modality.APPLICATION_MODAL);
		setResizable( false);
	}

	public static void update( Stage owner) {
		final AboutDialogFx dlg = new AboutDialogFx( owner);
		dlg.createMain();
		dlg.showDialog();
	}

	private void addEscapeAction() {
		getScene().setOnKeyPressed( key -> {
			if (key.getCode() == KeyCode.ESCAPE) {
				close();
			}
		});
	}

	public void createMain() {
		HBox hori = new HBox();
		hori.setSpacing( 5);
		ImageView logo = new ImageView( ResourceManager.getImageFx( "Krakel1.png"));
		Text msg = new Text( ResourceManager.getString( "config.about") + VersionHelper.VERSION);
		msg.setWrappingWidth( 250);
		hori.getChildren().addAll( logo, msg);
		//
		Button btnOk = new Button( "OK");
		btnOk.setAlignment( Pos.CENTER);
		btnOk.setOnAction( event -> close());
		BorderPane pane = new BorderPane();
		pane.setCenter( btnOk);
		//
		VBox vert = new VBox();
		vert.setPadding( new Insets( 10, 10, 10, 10));
		vert.setSpacing( 10);
		vert.getChildren().addAll( hori, pane);
		Scene scene = new Scene( vert);
		setScene( scene);
		addEscapeAction();
	}

	private void setTheme( String theme) {
		if (theme != null) {
			setTitle( ResourceManager.getString( theme));
		}
	}

	private void showDialog() {
		sizeToScene();
		centerOnScreen();
		showAndWait();
	}
}
