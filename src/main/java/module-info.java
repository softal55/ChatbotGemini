module com.example.chatbotgemini {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires com.google.gson;


    opens com.example.chatbotgemini to javafx.fxml;
    exports com.example.chatbotgemini;
}