package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageTableCell<S> extends TableCell<S, String> {
    final ImageView imageView = new ImageView();

    ImageTableCell() {
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty || item == null || item=="") {
            imageView.setImage(null);
            setGraphic(null);
        } else {
            imageView.setImage(new Image(item));
            setGraphic(imageView);
        }
    }
}