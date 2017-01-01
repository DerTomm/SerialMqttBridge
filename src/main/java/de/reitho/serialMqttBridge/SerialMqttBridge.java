package de.reitho.serialMqttBridge;

import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.reitho.serialMqttBridge.config.ConfigHandler;
import de.reitho.serialMqttBridge.plugins.MqttPublishPreprocessingPlugin;
import de.reitho.serialMqttBridge.plugins.SerialSendPreprocessingPlugin;
import de.reitho.serialMqttBridge.serial.SerialHandler;

public class SerialMqttBridge {

  Logger logger = LoggerFactory.getLogger(SerialMqttBridge.class);

  private MqttPublishPreprocessingPlugin mqttPublishPreprocessor;
  private SerialSendPreprocessingPlugin serialSendPreprocessor;

  private ConfigHandler configHandler;
  private SerialHandler serialHandler;

  /*********************************************************************************************************************************************************************
   * @param args
   */
  public static void main(String[] args) {
    new SerialMqttBridge().launch();
  }

  /*********************************************************************************************************************************************************************
   *
   */
  public SerialMqttBridge() {}

  /*********************************************************************************************************************************************************************
   *
   */
  public void launch() {

    try {

      // Get config handler instance
      logger.info("Reading configuration file...");
      configHandler = ConfigHandler.getInstance();

      // Instantiate preprocessor plugins if defined in config
      loadPlugins();

      System.out.println(mqttPublishPreprocessor);
      System.out.println(serialSendPreprocessor);

      // Establish serial connection
      logger.info("Building serial handler and establish connection...");
      serialHandler = SerialHandler.getInstance(configHandler);

    }
    catch (Exception e) {
      logger.error("An error occured.", e);
      System.exit(1);
    }

  }

  /*********************************************************************************************************************************************************************
   *
   */
  private void loadPlugins() throws Exception {

    Reflections reflections = new Reflections();

    /*
     *  Find all MQTT publishing preprocessor plugins and load the one the user defined in configuration file - otherwise skip plugin instantiation.
     */
    String configuredMqttPreprocessorPlugin = configHandler.getMqttPublishPreprocessorPlugin();

    if (configuredMqttPreprocessorPlugin != null && configuredMqttPreprocessorPlugin.length() > 1) {
      Set<Class<? extends MqttPublishPreprocessingPlugin>> mqttPreprocessorPlugins = reflections.getSubTypesOf(MqttPublishPreprocessingPlugin.class);
      for (Class<? extends MqttPublishPreprocessingPlugin> c : mqttPreprocessorPlugins) {
        if (c.getName().equalsIgnoreCase(configuredMqttPreprocessorPlugin)) {
          mqttPublishPreprocessor = c.newInstance();
          break;
        }
      }
      if (mqttPublishPreprocessor == null) {
        throw new Exception("Defined MQTT publish preproecessor plugin could not be found.");
      }
    }

    /*
     *  Find all serial send plugins and load the one the user defined in configuration file - otherwise skip plugin instantiation.
     */
    String configuredSerialPreprocessorPlugin = configHandler.getSerialSendPreprocessorPlugin();

    if (configuredSerialPreprocessorPlugin != null && configuredSerialPreprocessorPlugin.length() > 1) {
      Set<Class<? extends SerialSendPreprocessingPlugin>> serialPreprocessorPlugins = reflections.getSubTypesOf(SerialSendPreprocessingPlugin.class);
      for (Class<? extends SerialSendPreprocessingPlugin> c : serialPreprocessorPlugins) {
        if (c.getName().equalsIgnoreCase(configuredSerialPreprocessorPlugin)) {
          serialSendPreprocessor = c.newInstance();
          break;
        }
      }
      if (serialSendPreprocessor == null) {
        throw new Exception("Defined serial send preproecessor plugin could not be found.");
      }
    }
  }
}
