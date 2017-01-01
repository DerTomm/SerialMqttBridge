package de.reitho.serialMqttBridge.serial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.reitho.serialMqttBridge.config.ConfigHandler;
import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialHandler {

  private Logger logger = LoggerFactory.getLogger(SerialHandler.class);

  private static SerialHandler instance;
  private ConfigHandler configHandler;

  private SerialPort serialPort;

  /*********************************************************************************************************************************************************************
   * @param configHandler
   */
  private SerialHandler(ConfigHandler configHandler) throws Exception {
    this.configHandler = configHandler;
    establishSerialConnection();
  }

  /*********************************************************************************************************************************************************************
   * @param configHandler
   * @return
   */
  public static SerialHandler getInstance(ConfigHandler configHandler) throws Exception {
    if (instance == null) {
      instance = new SerialHandler(configHandler);
    }
    return instance;
  }

  /*********************************************************************************************************************************************************************
   * @throws SerialPortException
   */
  private void establishSerialConnection() throws SerialPortException {

    serialPort = new SerialPort(configHandler.getSerialPort());
    serialPort.openPort();
    serialPort.setParams(configHandler.getBaudRate(), configHandler.getDataBits(), configHandler.getStopBits(), configHandler.getParity());
    serialPort.addEventListener(new SerialPortListener(this));
  }

  /*********************************************************************************************************************************************************************
   * @return serial port handle
   */
  public SerialPort getSerialPort() {
    return serialPort;
  }

  /*********************************************************************************************************************************************************************
   * @param message
   */
  public void processMessage(String message) {
    logger.info("Serial/IN: " + message);
  }

}
