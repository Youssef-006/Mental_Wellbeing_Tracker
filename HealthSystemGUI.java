import javafx.application.Application;import javafx.geometry.Insets;import javafx.geometry.Pos;import javafx.scene.Scene;import javafx.scene.control.*;import javafx.scene.layout.*;import javafx.scene.text.Font;import javafx.scene.text.FontWeight;import javafx.stage.Stage;import javafx.scene.effect.DropShadow;import javafx.scene.paint.Color;import java.io.*;import java.util.ArrayList;import java.util.Optional;
import javafx.scene.image.Image;
public class HealthSystemGUI extends Application {
    private ArrayList<Person> users = new ArrayList<>();
    private final String USERS_FILE = "users.txt";
    private Stage primaryStage;
    // Modern color scheme - soft and vibrant
    private static final String PRIMARY_BG = "#f0f9ff"; // Light blue background
    private static final String SECONDARY_BG = "#e0f2fe"; // Lighter blue
    private static final String CARD_BG = "#ffffff";
    private static final String PRIMARY_COLOR = "#0c4a6e"; // Deep blue text
    private static final String SECONDARY_COLOR = "#0369a1"; // Medium blue
    private static final String ACCENT_COLOR = "#0ea5e9"; // Bright blue
    private static final String BORDER_COLOR = "#bae6fd"; // Soft blue border
    private static final String HOVER_BG = "#7dd3fc"; // Bright blue hover
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        // Set custom title (this will show in taskbar and window title)
        primaryStage.setTitle("Mind Flow");
        loadUsers();
        showMainMenu();
        primaryStage.setOnCloseRequest(e -> {
            saveUsers();
        });

        try {
            Image icon = new Image("https://em-content.zobj.net/source/apple/391/brain_1f9e0.png");
            // Request 256x256 size
            Image scaledIcon = new Image("https://em-content.zobj.net/source/apple/391/brain_1f9e0.png", 256, 256, true, true);
            primaryStage.getIcons().add(scaledIcon);
        } catch (Exception e) {
            System.out.println("Could not load icon");
        }

    }
    // ============ MAIN MENU ============
    private void showMainMenu() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SECONDARY_BG + ";");

        VBox layout = new VBox(30);
        layout.setPadding(new Insets(60));
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(550);

        // Title section
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);

        Label title = new Label("Mind Flow");
        title.setFont(Font.font("System", FontWeight.BOLD, 34));
        title.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");
        title.setWrapText(true);
        title.setMaxWidth(180);

        Label subtitle = new Label("The Mental Wellbeing Tracker For Your Wellness Journey");
        subtitle.setFont(Font.font("System", 16));
        subtitle.setStyle("-fx-text-fill: " + SECONDARY_COLOR + ";");
        subtitle.setWrapText(true);

        titleBox.getChildren().addAll(title, subtitle);

        // Buttons card
        VBox buttonCard = createCard();
        buttonCard.setSpacing(15);
        buttonCard.setPadding(new Insets(35, 50, 35, 50));
        buttonCard.setMaxWidth(450);

        Button addUserBtn = createModernButton("Add New User", false);
        Button selectUserBtn = createModernButton("Select User", true);
        Button exitBtn = createModernButton("Exit", false);

        addUserBtn.setMaxWidth(Double.MAX_VALUE);
        selectUserBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setMaxWidth(Double.MAX_VALUE);

        addUserBtn.setOnAction(e -> showAddUserDialog());
        selectUserBtn.setOnAction(e -> showSelectUserScreen());
        exitBtn.setOnAction(e -> {
            saveUsers();
            primaryStage.close();
        });
        buttonCard.getChildren().addAll(addUserBtn, selectUserBtn, exitBtn);

        layout.getChildren().addAll(titleBox, buttonCard);
        root.setCenter(layout);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mind Flow");
        primaryStage.show();
    }
    // ============ ADD USER DIALOG ============
    private void showAddUserDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Create a new user account");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(30));
        grid.setStyle("-fx-background-color: " + PRIMARY_BG + ";");

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + PRIMARY_COLOR + ";");
        TextField username = new TextField();
        username.setPromptText("Enter username");
        styleTextField(username);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + PRIMARY_COLOR + ";");
        PasswordField password = new PasswordField();
        password.setPromptText("Enter password");
        styleTextField(password);

        grid.add(usernameLabel, 0, 0);
        grid.add(username, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Style dialog buttons
        dialog.getDialogPane().setStyle("-fx-background-color: " + PRIMARY_BG + ";");

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = username.getText().trim();
            String pass = password.getText().trim();

            if (!name.isEmpty() && !pass.isEmpty()) {
                users.add(new Person(name, pass));
                saveUsers();
                showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Username and password cannot be empty!");
            }
        }
    }
    // ============ SELECT USER SCREEN ============
    private void showSelectUserScreen() {
        if (users.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Users", "No users available. Please add a user first.");
            return;
        }

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SECONDARY_BG + ";");

        VBox layout = new VBox(25);
        layout.setPadding(new Insets(60));
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(650);

        VBox headerBox = new VBox(8);
        headerBox.setAlignment(Pos.CENTER);

        Label title = new Label("Select User");
        title.setFont(Font.font("System", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");

        Label subtitle = new Label("Choose your profile to continue");
        subtitle.setFont(Font.font("System", 14));
        subtitle.setStyle("-fx-text-fill: " + SECONDARY_COLOR + ";");

        headerBox.getChildren().addAll(title, subtitle);

        VBox listCard = createCard();
        listCard.setPadding(new Insets(25));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(350);

        VBox userCardsContainer = new VBox(12);
        userCardsContainer.setPadding(new Insets(5));

        for (Person p : users) {
            HBox userCard = new HBox(15);
            userCard.setAlignment(Pos.CENTER_LEFT);
            userCard.setPadding(new Insets(18, 20, 18, 20));
            userCard.setStyle(
                    "-fx-background-color: " + SECONDARY_BG + ";" +
                            "-fx-background-radius: 10px;" +
                            "-fx-border-color: " + BORDER_COLOR + ";" +
                            "-fx-border-radius: 10px;" +
                            "-fx-border-width: 1.5px;" +
                            "-fx-cursor: hand;"
            );

            Label userIcon = new Label("👤");
            userIcon.setFont(Font.font(24));

            VBox userInfo = new VBox(3);
            Label userName = new Label(p.getName());
            userName.setFont(Font.font("System", FontWeight.BOLD, 16));
            userName.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");

            Label userSubtext = new Label("Click to login");
            userSubtext.setFont(Font.font("System", 12));
            userSubtext.setStyle("-fx-text-fill: " + SECONDARY_COLOR + ";");

            userInfo.getChildren().addAll(userName, userSubtext);
            HBox.setHgrow(userInfo, Priority.ALWAYS);

            Label arrow = new Label("→");
            arrow.setFont(Font.font(20));
            arrow.setStyle("-fx-text-fill: " + ACCENT_COLOR + ";");

            userCard.getChildren().addAll(userIcon, userInfo, arrow);

            userCard.setOnMouseEntered(e -> {
                userCard.setStyle(
                        "-fx-background-color: " + HOVER_BG + ";" +
                                "-fx-background-radius: 10px;" +
                                "-fx-border-color: " + ACCENT_COLOR + ";" +
                                "-fx-border-radius: 10px;" +
                                "-fx-border-width: 2px;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(14, 165, 233, 0.25), 8, 0, 0, 2);"
                );
            });

            userCard.setOnMouseExited(e -> {
                userCard.setStyle(
                        "-fx-background-color: " + SECONDARY_BG + ";" +
                                "-fx-background-radius: 10px;" +
                                "-fx-border-color: " + BORDER_COLOR + ";" +
                                "-fx-border-radius: 10px;" +
                                "-fx-border-width: 1.5px;" +
                                "-fx-cursor: hand;"
                );
            });

            userCard.setOnMouseClicked(e -> showPasswordDialog(p));

            userCardsContainer.getChildren().add(userCard);
        }

        scrollPane.setContent(userCardsContainer);
        listCard.getChildren().add(scrollPane);

        Button backBtn = createModernButton("Back", false);
        backBtn.setMaxWidth(300);
        backBtn.setOnAction(e -> showMainMenu());

        HBox backBox = new HBox(backBtn);
        backBox.setAlignment(Pos.CENTER);
        backBox.setPadding(new Insets(15, 0, 0, 0));

        layout.getChildren().addAll(headerBox, listCard, backBox);
        root.setCenter(layout);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }
    // ============ PASSWORD DIALOG ============
    private void showPasswordDialog(Person user) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Authentication");
        dialog.setHeaderText("Enter password for " + user.getName());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleTextField(passwordField);

        grid.add(new Label("Password:"), 0, 0);
        grid.add(passwordField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle("-fx-background-color: " + PRIMARY_BG + ";");

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (user.checkPassword(passwordField.getText())) {
                showUserDashboard(user);
            } else {
                showAlert(Alert.AlertType.ERROR, "Wrong Password", "Incorrect password. Try again.");
                showPasswordDialog(user);
            }
        }
    }
    // ============ USER DASHBOARD ============
    private void showUserDashboard(Person user) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SECONDARY_BG + ";");

        VBox layout = new VBox(30);
        layout.setPadding(new Insets(60));
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(550);

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label title = new Label("Welcome back!");
        title.setFont(Font.font("System", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");

        Label username = new Label(user.getName());
        username.setFont(Font.font("System", 20));
        username.setStyle("-fx-text-fill: " + SECONDARY_COLOR + ";");

        headerBox.getChildren().addAll(title, username);

        VBox buttonCard = createCard();
        buttonCard.setSpacing(15);
        buttonCard.setPadding(new Insets(35, 50, 35, 50));
        buttonCard.setMaxWidth(450);

        Button viewEntriesBtn = createModernButton("View Previous Entries", false);
        Button addEntryBtn = createModernButton("Add New Daily Entry", true);
        Button backBtn = createModernButton("Logout", false);

        viewEntriesBtn.setMaxWidth(Double.MAX_VALUE);
        addEntryBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setMaxWidth(Double.MAX_VALUE);

        viewEntriesBtn.setOnAction(e -> showViewEntriesScreen(user));
        addEntryBtn.setOnAction(e -> showAddEntryScreen(user));
        backBtn.setOnAction(e -> showMainMenu());

        buttonCard.getChildren().addAll(viewEntriesBtn, addEntryBtn, backBtn);

        layout.getChildren().addAll(headerBox, buttonCard);
        root.setCenter(layout);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }
    // ============ VIEW ENTRIES SCREEN ============
    private void showViewEntriesScreen(Person user) {
        ArrayList<String> entries = user.loadAllEntries();
        if (entries.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Entries", "No entries found for this user.");
            return;
        }
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SECONDARY_BG + ";");

        VBox layout = new VBox(25);
        layout.setPadding(new Insets(40));

        Label title = new Label("Your Health Entries");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");

        HBox contentBox = new HBox(20);

        // Left side - entry list
        VBox leftCard = createCard();
        leftCard.setPrefWidth(250);
        leftCard.setPadding(new Insets(20));

        Label listLabel = new Label("Select Day");
        listLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        listLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");

        ListView<String> entryList = new ListView<>();
        for (int i = 0; i < entries.size(); i++) {
            entryList.getItems().add("Day " + (i + 1));
        }
        entryList.setPrefHeight(400);
        entryList.setStyle(
                "-fx-background-color: " + SECONDARY_BG + ";" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-radius: 6px;" +
                        "-fx-background-radius: 6px;"
        );

        leftCard.getChildren().addAll(listLabel, entryList);
        // Right side - details
        VBox rightCard = createCard();
        rightCard.setPadding(new Insets(20));
        HBox.setHgrow(rightCard, Priority.ALWAYS);

        Label detailsLabel = new Label("Details");
        detailsLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        detailsLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");

        TextArea detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefHeight(400);
        detailsArea.setWrapText(true);
        detailsArea.setStyle(
                "-fx-control-inner-background: " + SECONDARY_BG + ";" + "-fx-font-family: 'Monospace';" + "-fx-font-size: 13px;" + "-fx-text-fill: " + PRIMARY_COLOR + ";" + "-fx-border-color: " + BORDER_COLOR + ";" + "-fx-border-radius: 6px;" + "-fx-background-radius: 6px;"
        );
        rightCard.getChildren().addAll(detailsLabel, detailsArea);
        entryList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int index = entryList.getSelectionModel().getSelectedIndex();
                detailsArea.setText(entries.get(index));
            }
        });
        contentBox.getChildren().addAll(leftCard, rightCard);

        Button backBtn = createModernButton("Back to Dashboard", false);
        backBtn.setOnAction(e -> showUserDashboard(user));

        HBox backBox = new HBox(backBtn);
        backBox.setAlignment(Pos.CENTER);
        backBox.setPadding(new Insets(10, 0, 0, 0));

        layout.getChildren().addAll(title, contentBox, backBox);
        root.setCenter(layout);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }
    // ============ ADD ENTRY SCREEN ============
    private void showAddEntryScreen(Person user) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SECONDARY_BG + ";");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + SECONDARY_BG + "; -fx-background-color: " + SECONDARY_BG + ";");

        VBox layout = new VBox(30);
        layout.setPadding(new Insets(40));
        layout.setMaxWidth(700);
        layout.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Daily Health Entry");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");

        // Form card
        VBox formCard = createCard();
        formCard.setPadding(new Insets(40));
        formCard.setSpacing(25);

        // Sleep Section
        VBox sleepSection = createSection("Sleep", "Hours of sleep (0-23)");
        TextField sleepField = new TextField();
        sleepField.setPromptText("e.g., 7.5");
        styleTextField(sleepField);
        sleepSection.getChildren().add(sleepField);

        // Stress Section
        VBox stressSection = createSection("Stress Assessment", "Answer the following questions");
        CheckBox procrastinate = new CheckBox("Did you procrastinate today?");
        CheckBox importantEvent = new CheckBox("Did you have an important event today?");
        styleCheckBox(procrastinate);
        styleCheckBox(importantEvent);

        VBox pressureBox = new VBox(8);
        Label pressureLabel = new Label("How pressured did you feel?");
        pressureLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + SECONDARY_COLOR + ";");
        Slider pressureSlider = createStyledSlider();

        VBox exhaustBox = new VBox(8);
        Label exhaustLabel = new Label("Emotional exhaustion level?");
        exhaustLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + SECONDARY_COLOR + ";");
        Slider exhaustSlider = createStyledSlider();

        CheckBox tiredAfterRest = new CheckBox("Did you feel tired even after resting?");
        styleCheckBox(tiredAfterRest);

        pressureBox.getChildren().addAll(pressureLabel, pressureSlider);
        exhaustBox.getChildren().addAll(exhaustLabel, exhaustSlider);

        stressSection.getChildren().addAll(procrastinate, importantEvent, pressureBox, exhaustBox, tiredAfterRest);
        // Water Section
        VBox waterSection = createSection("Water Intake", "Liters consumed (0-7)");
        TextField waterField = new TextField();
        waterField.setPromptText("e.g., 2.5");
        styleTextField(waterField);
        waterSection.getChildren().add(waterField);
        // Screen Time Section
        VBox screenSection = createSection("Screen Time", "Hours spent on screens");
        TextField productiveScreenField = new TextField();
        productiveScreenField.setPromptText("Productive screen time (e.g., 3)");
        styleTextField(productiveScreenField);
        TextField unproductiveScreenField = new TextField();
        unproductiveScreenField.setPromptText("Unproductive screen time (e.g., 2)");
        styleTextField(unproductiveScreenField);
        screenSection.getChildren().addAll(productiveScreenField, unproductiveScreenField);

        // Movement Section
        VBox movementSection = createSection("Movement", "Estimated steps today");
        TextField stepsField = new TextField();
        stepsField.setPromptText("e.g., 8000");
        styleTextField(stepsField);
        movementSection.getChildren().add(stepsField);

        // Notes Section
        VBox notesSection = createSection("Additional Notes", "Any extra observations");
        TextArea notesArea = new TextArea();
        notesArea.setPrefRowCount(4);
        notesArea.setPromptText("Write any additional notes here...");
        notesArea.setStyle(
                "-fx-control-inner-background: " + SECONDARY_BG + ";" + "-fx-font-size: 14px;" + "-fx-border-color: " + BORDER_COLOR + ";" + "-fx-border-radius: 8px;" + "-fx-background-radius: 8px;" + "-fx-padding: 12px;"
        );
        notesSection.getChildren().add(notesArea);
        formCard.getChildren().addAll(
                sleepSection, createSeparator(), stressSection, createSeparator(), waterSection, createSeparator(), screenSection, createSeparator(), movementSection, createSeparator(), notesSection
        );
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button submitBtn = createModernButton("Analyze & Continue", true);
        Button cancelBtn = createModernButton("Cancel", false);

        submitBtn.setOnAction(e -> {
            try {
                double sleep = Double.parseDouble(sleepField.getText());
                double water = Double.parseDouble(waterField.getText());
                double productiveScreen = Double.parseDouble(productiveScreenField.getText());
                double unproductiveScreen = Double.parseDouble(unproductiveScreenField.getText());
                double steps = Double.parseDouble(stepsField.getText());

                double stressScore = calculateStress(
                        procrastinate.isSelected(), importantEvent.isSelected(), pressureSlider.getValue(), exhaustSlider.getValue(), tiredAfterRest.isSelected()
                );

                String notes = notesArea.getText();

                // Show advice screen before saving
                showAdviceScreen(user, sleep, stressScore, water,
                        productiveScreen, unproductiveScreen, steps, notes);

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input",
                        "Please enter valid numbers for all fields.");
            }
        });

        cancelBtn.setOnAction(e -> showUserDashboard(user));

        buttonBox.getChildren().addAll(submitBtn, cancelBtn);

        layout.getChildren().addAll(title, formCard, buttonBox);

        VBox wrapper = new VBox(layout);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setStyle("-fx-background-color: " + SECONDARY_BG + ";");

        scrollPane.setContent(wrapper);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 900, 800);
        primaryStage.setScene(scene);
    }
    // ============ ADVICE SCREEN ============
    private void showAdviceScreen(Person user, double sleep, double stressScore, double water,
                                  double productiveScreen, double unproductiveScreen,
                                  double steps, String notes) {

        // Create scale instances and calculate
        SleepScale sleepScale = new SleepScale();
        sleepScale.score = sleep;
        sleepScale.indicator(sleep);

        StressScale stressScale = new StressScale();
        stressScale.score = stressScore;
        stressScale.indicator(stressScore);

        WaterIntakeScale waterScale = new WaterIntakeScale();
        waterScale.score = water;
        waterScale.indicator(water);

        ScreenTimeScale screenScale = new ScreenTimeScale();
        screenScale.score = unproductiveScreen;
        double totalScreen = productiveScreen + unproductiveScreen;
        if (totalScreen != 0) {
            screenScale.productivePercent = (productiveScreen / totalScreen) * 100;
        } else {
            screenScale.productivePercent = 300; // INVALID_PERCENT
        }
        screenScale.indicator(unproductiveScreen);

        MovementScale movementScale = new MovementScale();
        movementScale.score = steps;
        movementScale.indicator(steps);

        MoodScale moodScale = new MoodScale();
        moodScale.indicator(sleepScale, stressScale);

        FocusScale focusScale = new FocusScale();
        focusScale.indicator(sleepScale, screenScale);

        // Build UI
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + SECONDARY_BG + ";");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + SECONDARY_BG + "; -fx-background-color: " + SECONDARY_BG + ";");

        VBox layout = new VBox(30);
        layout.setPadding(new Insets(40));
        layout.setMaxWidth(800);
        layout.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Your Health Analysis");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");

        Label subtitle = new Label("Here's what your data tells us about your wellness today");
        subtitle.setFont(Font.font("System", 15));
        subtitle.setStyle("-fx-text-fill: " + SECONDARY_COLOR + ";");
        subtitle.setWrapText(true);
        VBox adviceCard = createCard();
        adviceCard.setPadding(new Insets(35));
        adviceCard.setSpacing(20);
        // Add all advice sections
        adviceCard.getChildren().addAll(
                createAdviceSection("Sleep", sleepScale.getStatus(), getSleepAdvice(sleepScale.getStatus())),
                createSeparator(),
                createAdviceSection("Stress", stressScale.getStatus(),
                        getStressAdvice(stressScale.getStatus(), stressScore)),
                createSeparator(),
                createAdviceSection("Water Intake", waterScale.getStatus(), getWaterAdvice(waterScale.getStatus())),
                createSeparator(),
                createAdviceSection("Screen Time", screenScale.getStatus(),
                        getScreenAdvice(screenScale.getStatus(), screenScale.productivePercent)),
                createSeparator(),
                createAdviceSection("Movement", movementScale.getStatus(), getMovementAdvice(movementScale.getStatus())),
                createSeparator(),
                createAdviceSection("Mood", moodScale.getStatus(), getMoodAdvice(moodScale.getStatus())),
                createSeparator(),
                createAdviceSection("Focus", focusScale.getStatus(), getFocusAdvice(focusScale.getStatus()))
        );
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button saveBtn = createModernButton("Save Entry", true);
        Button backBtn = createModernButton("Back to Edit", false);

        saveBtn.setOnAction(e -> {
            user.saveDailyEntryFromGUI(sleep, stressScore, water,
                    productiveScreen, unproductiveScreen, steps, notes);
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Daily entry saved successfully!");
            showUserDashboard(user);
        });
        backBtn.setOnAction(e -> showAddEntryScreen(user));

        buttonBox.getChildren().addAll(saveBtn, backBtn);

        layout.getChildren().addAll(title, subtitle, adviceCard, buttonBox);

        VBox wrapper = new VBox(layout);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setStyle("-fx-background-color: " + SECONDARY_BG + ";");

        scrollPane.setContent(wrapper);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 900, 800);
        primaryStage.setScene(scene);
    }
    // ============ HELPER METHODS ============
    private VBox createCard() {
        VBox card = new VBox();
        card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" + "-fx-background-radius: 16px;" + "-fx-border-color: " + BORDER_COLOR + ";" + "-fx-border-radius: 16px;" + "-fx-border-width: 2px;" + "-fx-effect: dropshadow(gaussian, rgba(14, 165, 233, 0.15), 15, 0, 0, 4);"
        );
        return card;
    }
    private VBox createSection(String title, String subtitle) {
        VBox section = new VBox(10);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + SECONDARY_COLOR + ";");

        section.getChildren().addAll(titleLabel, subtitleLabel);
        return section;
    }
    private Separator createSeparator() {
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + BORDER_COLOR + ";");
        return sep;
    }
    private Slider createStyledSlider() {
        Slider slider = new Slider(1, 5, 3);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
        slider.setStyle("-fx-control-inner-background: " + SECONDARY_BG + ";");
        return slider;
    }
    private void styleTextField(TextField field) {
        field.setStyle(
                "-fx-background-color: " + SECONDARY_BG + ";" + "-fx-border-color: " + BORDER_COLOR + ";" + "-fx-border-radius: 8px;" + "-fx-background-radius: 8px;" + "-fx-padding: 12px;" + "-fx-font-size: 14px;" + "-fx-text-fill: " + PRIMARY_COLOR + ";"
        );
        field.setMinWidth(250);
    }
    private void styleCheckBox(CheckBox checkBox) {
        checkBox.setStyle(
                "-fx-font-size: 14px;" + "-fx-text-fill: " + PRIMARY_COLOR + ";"
        );
    }
    private double calculateStress(boolean proc, boolean event, double pressure, double exhaust, boolean tired) {
        double sum = 0; sum += proc ? 5.0 : 1.0;sum += event ? 5.0 : 1.0;sum += pressure;sum += exhaust;sum += tired ? 5.0 : 1.0;return sum / 5.0;
    }
    // ============ ADVICE SECTION HELPER ============
    private VBox createAdviceSection(String category, String status, String advice) {
        VBox section = new VBox(8);
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Label categoryLabel = new Label(category);
        categoryLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        categoryLabel.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";");
        Label statusLabel = new Label(status.replace("_", " "));
        statusLabel.setStyle(
                "-fx-background-color: " + getStatusColor(status) + ";" + "-fx-text-fill: white;" + "-fx-padding: 4px 12px;" + "-fx-background-radius: 12px;" + "-fx-font-size: 12px;" + "-fx-font-weight: 600;"
        );
        headerBox.getChildren().addAll(categoryLabel, statusLabel);
        Label adviceLabel = new Label(advice);
        adviceLabel.setWrapText(true);
        adviceLabel.setStyle(
                "-fx-text-fill: " + SECONDARY_COLOR + ";" + "-fx-font-size: 14px;" + "-fx-line-spacing: 2px;"
        );
        section.getChildren().addAll(headerBox, adviceLabel);
        return section;
    }
    // ============ STATUS COLOR HELPER ============
    private String getStatusColor(String status) {
        if (status.contains("PERFECT") || status.contains("GOOD") || status.contains("HAPPY") ||
                status.contains("CALM") || status.contains("HIGH_FOCUS") || status.contains("CONTENT")) {
            return "#10b981"; // Emerald green
        } else if (status.contains("LOW") || status.contains("HIGH") || status.contains("BAD") ||
                status.contains("IRRITABLE") || status.contains("VERY_LOW")) {
            return "#f43f5e"; // Rose red
        } else {
            return "#f59e0b"; // Amber orange
        }
    }
    // ============ ADVICE TEXT GENERATORS ============
    private String getSleepAdvice(String status) {
        switch (status) {
            case "LOW_SLEEP":
                return "Your sleep has been too low. Try to lower caffeine intake and establish a consistent bedtime routine.";
            case "HIGH_SLEEP":
                return "Your sleep has been too high. Try using an alarm clock and maintaining a regular sleep schedule.";
            default:
                return "Perfect sleep! Continue maintaining this healthy sleep pattern.";
        }
    }
    private String getStressAdvice(String status, double score) {
        String base = String.format("Your stress level is %.1f out of 5. ", score);
        switch (status) {
            case "HIGH_STRESS":
                return base + "Consider taking breaks, practicing deep breathing, and delegating tasks when possible.";
            case "MILD_STRESS":
                return base + "Manage stress with light exercise, adequate rest, and time management techniques.";
            default:
                return base + "Perfect! Keep up your stress management practices.";
        }
    }
    private String getWaterAdvice(String status) {
        switch (status) {
            case "LOW_INTAKE":
                return "Too little water. Try to drink more - take small sips every hour to stay hydrated.";
            case "HIGH_INTAKE":
                return "Too much water. Slow down and spread your drinking throughout the day to avoid overhydration.";
            default:
                return "Good water intake! Keep drinking steadily through the day.";
        }
    }
    private String getScreenAdvice(String status, double productivePercent) {
        if (productivePercent == 300) {
            return "The productivity and screen time calculation can't be measured.";
        }
        String base = String.format("Productive screen time: %.1f%%. ", productivePercent);
        switch (status) {
            case "BAD_ST":
                return base + "Be careful! Too much unproductive screen time can waste your day. Set timers for apps, turn off notifications, and use a to-do list to stay focused.";
            case "NORMAL_ST":
                return base + "Good, but try to reduce unproductive time a little to increase productivity.";
            default:
                return base + "Excellent! You have great self-control and healthy digital habits.";
        }
    }
    private String getMovementAdvice(String status) {
        switch (status) {
            case "LOW_STEPS":
                return "Your steps have been too low. Try to aim for 4,000 to 5,000 steps per day.";
            case "HIGH_STEPS":
                return "Your steps have been high - that's great! Just make sure to rest adequately.";
            default:
                return "Perfect step count! Continue this active lifestyle.";
        }
    }
    private String getMoodAdvice(String status) {
        String base = "Based on your sleep and stress levels, your predicted mood is " + status.replace("_", " ") + ". ";
        switch (status) {
            case "VERY_IRRITABLE":
                return base + "Try resting early and reducing stressful tasks to improve your mood.";
            case "IRRITABLE":
                return base + "A short walk or breathing exercise can help lift your spirits.";
            case "NEUTRAL":
                return base + "Try some light relaxation or music to enhance your wellbeing.";
            case "CALM":
                return base + "Great job managing stress!";
            case "CONTENT":
                return base + "Keep your healthy routine going!";
            case "HAPPY":
                return base + "Keep up the good habits!";
            default:
                return "Mood analysis complete.";
        }
    }
    private String getFocusAdvice(String status) {
        String base = "Based on your screen time and sleep, your predicted focus level is " + status.replace("_", " ") + ". ";
        switch (status) {
            case "VERY_LOW_FOCUS":
                return base + "Reduce distractions and get some rest to improve concentration.";
            case "LOW_FOCUS":
                return base + "Try a Pomodoro session and remove phone distractions.";
            case "AVERAGE_FOCUS":
                return base + "A clear plan for the day will improve your productivity.";
            case "HIGH_FOCUS":
                return base + "Keep up the healthy habits!";
            default:
                return "Focus analysis complete.";
        }
    }
    private Button createModernButton(String text, boolean primary) {
        Button btn = new Button(text);
        btn.setMinWidth(250);
        btn.setMinHeight(45);
        if (primary) {
            btn.setStyle(
                    "-fx-background-color: " + ACCENT_COLOR + ";" + "-fx-text-fill: white;" + "-fx-font-size: 15px;" + "-fx-font-weight: 600;" + "-fx-padding: 12px 24px;" + "-fx-background-radius: 8px;" + "-fx-cursor: hand;"
            );
            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: #0b5ed7;" + "-fx-text-fill: white;" + "-fx-font-size: 15px;" + "-fx-font-weight: 600;" + "-fx-padding: 12px 24px;" + "-fx-background-radius: 8px;" + "-fx-cursor: hand;"
            ));
            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: " + ACCENT_COLOR + ";" + "-fx-text-fill: white;" + "-fx-font-size: 15px;" + "-fx-font-weight: 600;" + "-fx-padding: 12px 24px;" + "-fx-background-radius: 8px;" + "-fx-cursor: hand;"
            ));
        } else {
            btn.setStyle(
                    "-fx-background-color: " + PRIMARY_BG + ";" + "-fx-text-fill: " + PRIMARY_COLOR + ";" + "-fx-font-size: 15px;" + "-fx-padding: 12px 24px;" + "-fx-background-radius: 8px;" + "-fx-border-color: " + BORDER_COLOR + ";" + "-fx-border-radius: 8px;" + "-fx-border-width: 1.5px;" + "-fx-cursor: hand;"
            );
            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: " + HOVER_BG + ";" +
                            "-fx-text-fill: " + PRIMARY_COLOR + ";" + "-fx-font-size: 15px;" + "-fx-padding: 12px 24px;" + "-fx-background-radius: 8px;" + "-fx-border-color: " + BORDER_COLOR + ";" + "-fx-border-radius: 8px;" + "-fx-border-width: 1.5px;" + "-fx-cursor: hand;"
            ));
            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: " + PRIMARY_BG + ";" + "-fx-text-fill: " + PRIMARY_COLOR + ";" + "-fx-font-size: 15px;" + "-fx-padding: 12px 24px;" + "-fx-background-radius: 8px;" + "-fx-border-color: " + BORDER_COLOR + ";" + "-fx-border-radius: 8px;" + "-fx-border-width: 1.5px;" + "-fx-cursor: hand;"
            ));
        }
        return btn;
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("-fx-background-color: " + PRIMARY_BG + ";");
        alert.showAndWait();
    }
    // ============ FILE OPERATIONS ============
    public void loadUsers() {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2)
                    users.add(new Person(parts[0], parts[1]));
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading users.");
        }
    }
    public void saveUsers() {
        try (FileWriter fw = new FileWriter(USERS_FILE)) {
            for (Person p : users) {
                fw.write(p.getName() + "," + p.getPassword() + "\n");
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving users.");
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}