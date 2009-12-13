package nl.iljabooij.garmintrainer.parser

import java.io.InputStream
import nl.iljabooij.garmintrainer.model.Activity

/**
 * Parse the {@link InputStream} and return the resulting list of Activities.
 * @param inputStream the input stream to take input from. Note that the TcxParser
 * does not have the responsibility of closing the input stream.
 * @return a list of Activities.
 * @throws ParseException if an error occured during parsing.
 */
trait TcxParser {
  def parse(inputStream: InputStream): List[Activity]
}
