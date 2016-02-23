package controllers

import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.oned.Code128Writer
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.{BarcodeFormat, EncodeHintType}
import play.api.libs.iteratee.Enumerator
import play.api.mvc._

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {
  val QRHints = Map(EncodeHintType.CHARACTER_SET -> "UTF-8", EncodeHintType.MARGIN -> 0)

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def qrcode = Action { implicit req =>
    req.getQueryString("code") match {
      case Some(code) =>
        val writer = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, 250, 250, QRHints)
        val data = Enumerator.outputStream { out =>
          MatrixToImageWriter.writeToStream(writer, "png", out)
          out.close()
        }
        Ok.chunked(data).as("image/png")
      case None => NotFound
    }
  }

  def barcode = Action { implicit req =>
    req.getQueryString("code") match {
      case Some(code) =>
        val bitMatrix = new Code128Writer().encode(code, BarcodeFormat.CODE_128, 250, 100, QRHints)
        val data = Enumerator.outputStream { out =>
          MatrixToImageWriter.writeToStream(bitMatrix, "png", out)
          out.close()
        }
        Ok.chunked(data).as("image/png")
      case None => NotFound
    }
  }

  /**
    * 算法:
    * 10 - (sum(奇数位) + 3 * sum(偶数位)) % 10
    * @param str 输入参数
    * @return 返回最后一位校验码
    */
  def checksum(str: String): Char = {
    val zip = str.zipWithIndex
    val odd = zip.foldLeft(0) { case (b, (c, i)) => if (i % 2 == 0) b + (c - '0') else b }
    val even = zip.foldLeft(0) { case (b, (c, i)) => if (i % 2 == 1) b + (c - '0') else b }
    ('0' + (10 - (odd + 3 * even) % 10)).toChar
  }
}
