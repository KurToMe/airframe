/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wvlet.log

import java.io.File

import wvlet.log.io.IOUtil._

/**
  *
  */
class LogRotationHandlerTest extends Spec {

  "LogRotationHandler" should {

    "rotate log files" in {
      val l = Logger("wvlet.log.rotation")
      withTempFile(name = "log-rotation-test", dir = "target") { f =>
        val h = new LogRotationHandler(f.getPath, 5, 10)
        l.resetHandler(h)

        l.info("test message")
        l.info("this logger handler rotates logs and compressed the log archives in .gz format")
        l.info("this is the end of log files")
        h.flush()
        h.close()

        f should exist
        f.length > 0 shouldBe true
      }
    }

    "rescue orphaned log files" in {
      val l   = Logger("wvlet.log.rotation")
      val tmp = new File("target/log-rotation-test.log.tmp")
      if (!tmp.exists()) {
        tmp.createNewFile()
      }
      tmp should exist
      val h = new LogRotationHandler("target/log-rotation-test.log", 5, 10)

      tmp shouldNot exist
    }
  }

  "FileHandler" should {
    "output log to a file" in {
      val l = Logger("wvlet.log.filehandler")
      withTempFile(name = "log-file-test", dir = "target") { f =>
        val h = new FileHandler(f.getPath)
        l.resetHandler(h)

        l.info("test message")
        l.info("this logger handler rotates logs and compressed the log archives in .gz format")
        l.info("this is the end of log files")
        h.flush()
        h.close()

        f should exist
        f.length > 0 shouldBe true
      }
    }
  }
}
