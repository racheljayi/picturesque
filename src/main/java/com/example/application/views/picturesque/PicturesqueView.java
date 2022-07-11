package com.example.application.views.picturesque;

import com.example.application.ImageLoader;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import java.net.URL;

@PageTitle("Picturesque")
@Route(value = "")

public class PicturesqueView extends VerticalLayout {
    TextField urlText = new TextField();
    IntegerField number = new IntegerField();
    IntegerField select = new IntegerField();
    Button button = new Button("GO!");
    Button cbutton = new Button("Clear");
    Notification error = new Notification();
    Notification welcome = new Notification();
    ImageLoader next = new ImageLoader("https://genesisairway.com/wp-content/uploads/2019/05/no-image.jpg", 10, 40);

    SplitLayout splitLayout = new SplitLayout();
    public PicturesqueView() {
        add(getToolBar());
        welcome.add(welcomeLayout());
        welcome.setPosition(Notification.Position.MIDDLE);
        welcome.open();
        clearButton();
    }

    private HorizontalLayout getToolBar(){
        Image logo = new Image("images/logo.png", "");

        urlText.setPlaceholder("Enter image address...");
        urlText.setClearButtonVisible(true);

        number.setHelperText("# of Colors");
        number.setValue(10);
        number.setHasControls(true);
        number.setMin(1);
        number.setMax(20);

        select.setHelperText("Saturation");
        select.setValue(40);
        select.setStep(5);
        select.setHasControls(true);
        select.setMin(10);
        select.setMax(80);

        error.addThemeVariants(NotificationVariant.LUMO_ERROR);

        button.addClickShortcut(Key.ENTER);
        button.setDisableOnClick(true);
        button.addClickListener(event -> {
            try {
                configurePalette(urlText.getValue(), number.getValue(), select.getValue());
            } catch (IOException e) {
                error.add(erLayout());
                error.open();
                throw new RuntimeException(e);
            }
        });

        HorizontalLayout toolbar = new HorizontalLayout(logo, urlText, number, select, button, cbutton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configurePalette(String url, int num, int dif) throws IOException {
        error.addThemeVariants(NotificationVariant.LUMO_ERROR);

        try {
            button.setEnabled(true);
            next = new ImageLoader(url, num, dif);

            URL urlin = new URL(url);
            BufferedImage img = ImageIO.read(urlin);
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos1);
            byte[] mainByte = baos1.toByteArray();

            ArrayList<Color> colors = next.PaletteLoader();
            VerticalLayout layout = new VerticalLayout();
            layout.setPadding(false);

            for (Color c : colors) {
                HorizontalLayout layout2 = new HorizontalLayout();
                layout2.setPadding(false);

                byte[] bytes = getImage(c);

                VerticalLayout content = new VerticalLayout(new Text("#" + Integer.toHexString(c.getRGB()).substring(2)));
                content.setSpacing(false);
                content.setPadding(false);

                Details details = new Details("", content);
                details.setOpened(false);

                layout2.add(convertToImage(bytes));
                layout2.add(details);
                layout.add(layout2);
            }
            splitLayout = new SplitLayout(convertToImage(mainByte), layout);
            splitLayout.setMaxWidth("1880px");
            splitLayout.setMaxHeight("780px");
            add(splitLayout);
        }
        catch (IOException e){
            error.add(erLayout());
            error.open();
            throw new RuntimeException(e);
        }
    }

    private VerticalLayout welcomeLayout(){
        VerticalLayout wL = new VerticalLayout();

        Div heading = new Div(new Text("Welcome to Picturesque 🎨"));
        heading
                .getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("font-weight", "600");

        Div info1 = new Div(new Text("Enter a image url to get started!"));

        VerticalLayout list = new VerticalLayout();
        Div tip1 = new Div(new Text("• View hex codes by pressing '>' next to each color"));
        Div tip2 = new Div(new Text("• Adjust your layout by dragging the slider between each image and palette"));
        Div tip3 = new Div(new Text("• Press 'Clear' to clear your screen!"));
        list.add(tip1, tip2, tip3);
        list
                .getStyle()
                .set("font-size", "var(--lumo-font-size-s)")
                .set("color", "var(--lumo-secondary-text-color)");

        Button closeButton = new Button(new Icon("lumo", "angle-down"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeButton.getElement().setAttribute("aria-label", "Let's Go!");
        closeButton.addClickListener(event -> welcome.close());

        Div all = new Div(heading, info1);

        wL.add(all, list, closeButton);
        wL.setAlignItems(Alignment.CENTER);
        return wL;
    }

    private HorizontalLayout erLayout(){
        Div text = new Div(new Text("Invalid Url"));

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> error.close());


        HorizontalLayout erLayout = new HorizontalLayout(text, closeButton);
        erLayout.setAlignItems(Alignment.CENTER);
        return erLayout;
    }

    private void clearButton(){
        cbutton.addClickListener( event -> {
            splitLayout.removeAll();
            removeAll();
            add(getToolBar());
        });
    }
    private byte[] getImage(Color c) throws IOException {
        BufferedImage bi = new BufferedImage(300, 50, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(c);
        g2d.fillRect(0, 0, 300, 50);
        ImageIO.write(bi, "jpg", baos2);
        return baos2.toByteArray();
    }

    private Image convertToImage(byte[] imageData) {
        StreamResource streamResource = new StreamResource("isr", (InputStreamFactory) () -> new ByteArrayInputStream(imageData));
        return new Image(streamResource, "color");
    }

}
