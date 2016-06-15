package co.quine.slibj

import java.io._
import java.nio.file.Paths
import fs2.util.Task

object DefaultModel {

  private def createTempFile(prefix: String, suffix: String) = {
    val tmp = File.createTempFile(prefix, suffix)
    tmp.deleteOnExit()
    tmp
  }

  def get = {
    val tmp = createTempFile("slib", ".dat")
    val res = getClass.getResource("/models/face_landmarks_68.dat")
    val reader = fs2.io.file.readAll[Task](Paths.get(res.getPath), 4096)
      .through(fs2.io.file.writeAll(Paths.get(tmp.getAbsolutePath)))
      .run
    reader.unsafeRun
    tmp
  }
}

