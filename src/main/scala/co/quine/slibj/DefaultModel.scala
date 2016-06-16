package co.quine.slibj

import java.io._
import java.nio.ByteBuffer
import java.nio.channels._
import java.nio.file._
import scalaj.http._

object DefaultModel {

  private val homeDir = System.getProperty("user.home")
  private val defaultModelUrl = "https://s3.amazonaws.com/models.quine.co/face_landmarks_68.dat"
  private val defaultModelRelPath = "/.slibj/models/face_landmarks_68.dat"
  private val defaultModelAbsPath = s"$homeDir$defaultModelRelPath"

  private def getTempFile(prefix: String, suffix: String) = {
    val tmp = File.createTempFile(prefix, suffix)
    tmp.deleteOnExit()
    tmp
  }

  private def modelExistsHome = {
    val modelHomePath = Paths.get(defaultModelAbsPath)
    Files.exists(modelHomePath)
  }

  def getModel = modelExistsHome match {
    case true => defaultModelAbsPath
    case false =>
      Files.createDirectories(Paths.get(s"$homeDir/.slibj/models"))
      val is = new ByteArrayInputStream(Http(defaultModelUrl).asBytes.body)
      val ic = Channels.newChannel(is)
      val os = new FileOutputStream(defaultModelAbsPath).getChannel
      val buffer = ByteBuffer.allocate(4096)

      try {
        while (ic.read(buffer) >= 0 || buffer.position() > 0) {
          buffer.flip()
          os.write(buffer)
          buffer.compact()
        }
      } finally {
        is.close()
        ic.close()
        os.close()
      }
      defaultModelAbsPath
  }
}