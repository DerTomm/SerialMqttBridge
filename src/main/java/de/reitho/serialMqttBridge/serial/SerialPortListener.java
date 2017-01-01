package de.reitho.serialMqttBridge.serial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialPortListener implements SerialPortEventListener {

  private Logger logger = LoggerFactory.getLogger(SerialPortListener.class);

  private SerialHandler serialHandler;
  private StringBuilder messageBuffer;

  /*********************************************************************************************************************************************************************
   * @param serialPort
   */
  public SerialPortListener(SerialHandler serialHandler) {
    this.serialHandler = serialHandler;
    messageBuffer = new StringBuilder();
  }

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see jssc.SerialPortEventListener#serialEvent(jssc.SerialPortEvent)
   */
  @Override
  public void serialEvent(SerialPortEvent event) {

    if (event.isRXCHAR() && event.getEventValue() > 0) {

      try {

        byte buffer[] = serialHandler.getSerialPort().readBytes();
        for (byte b : buffer) {

          if ((b == '\n') && messageBuffer.length() > 0) {
            String message = messageBuffer.toString().replaceAll("\r", "");
            serialHandler.processMessage(message);
            messageBuffer.setLength(0);
          }
          else {
            messageBuffer.append((char) b);
          }

        }
      }
      catch (SerialPortException ex) {
        logger.error("Exception", ex);
      }
    }
  }
}
