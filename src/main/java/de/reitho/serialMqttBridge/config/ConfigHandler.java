package de.reitho.serialMqttBridge.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHandler {

  private static ConfigHandler instance;

  private Properties prop;

  /* Serial connection properties */
  private String serialPort;
  private int baudRate;
  private int dataBits;
  private int stopBits;
  private int parity;

  /* Logging properties */
  private boolean logSerialMessages;

  /*********************************************************************************************************************************************************************
   * @throws Exception
   */
  private ConfigHandler() throws Exception {

    prop = new Properties();
    readConfigurationFile();
  }

  /*********************************************************************************************************************************************************************
   * @return
   */
  public static ConfigHandler getInstance() throws Exception {
    if (instance == null) {
      instance = new ConfigHandler();
    }
    return instance;
  }

  /*********************************************************************************************************************************************************************
   * @throws FileNotFoundException
   */
  private void readConfigurationFile() throws Exception {

    InputStream input = null;

    try {

      input = this.getClass().getResourceAsStream("/config.properties");
      prop.load(input);

      /* Parse serial connection properties */
      serialPort = prop.getProperty("serialPort");

      try {

        baudRate = Integer.parseInt(prop.getProperty("baudRate"));
        dataBits = Integer.parseInt(prop.getProperty("dataBits"));
        stopBits = Integer.parseInt(prop.getProperty("stopBits"));
        parity = Integer.parseInt(prop.getProperty("parity"));
      }
      catch (NumberFormatException e) {
        throw e;
      }

      /* Parse logging properties */
      logSerialMessages = Boolean.parseBoolean(prop.getProperty("logSerialMessages"));

    }
    catch (IOException ex) {
      throw ex;
    }
    finally {
      if (input != null) {
        try {
          input.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /*********************************************************************************************************************************************************************
   * @return serial port name
   */
  public String getSerialPort() {
    return serialPort;
  }

  /*********************************************************************************************************************************************************************
   * @return baudrate
   */
  public int getBaudRate() {
    return baudRate;
  }

  /*********************************************************************************************************************************************************************
   * @return data bits
   */
  public int getDataBits() {
    return dataBits;
  }

  /*********************************************************************************************************************************************************************
   * @return stop bits
   */
  public int getStopBits() {
    return stopBits;
  }

  /*********************************************************************************************************************************************************************
   * @return parity
   */
  public int getParity() {
    return parity;
  }

  /*********************************************************************************************************************************************************************
   * @return flag whether serial messages should be logged
   */
  public boolean logSerialMessages() {
    return logSerialMessages;
  }

}
