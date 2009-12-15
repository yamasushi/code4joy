// picure file handling class
// 2009-08-22 shuji yamamoto
// 2009-09-24 shuji
// 2009-09-30 shuji TYPE_USHORT_555_RGB
// 15:43 2009/11/06 divide into traits --> Picture,Canvas
// 18:57 2009/11/13 filename ---> File object
// 6:55 2009/12/16  TYPE_BYTE_GRAY
 
import java.awt._
import java.awt.image._
import javax.imageio._
import java.io._

class PictureFile( 
		val file     : File     ,
		val geom     : Geometry[Int],
		val imgType  : String , 
		val colorBG  : Color  ) extends Picture[BufferedImage]
{
	override def startPaint : BufferedImage = {
		new BufferedImage(geom.size.x,geom.size.y,BufferedImage.TYPE_BYTE_GRAY )
	}
	
	override def endPaint(bi:BufferedImage):Unit = {
		ImageIO.write(	bi , 
						imgType , 
						file)
	}
	
	override def paint(op:Graphics2D => Unit ):Unit = {
		doPaint({ bi:BufferedImage => 
					val g = bi.createGraphics()
					g.setPaint(colorBG)
					g.fillRect(0,0,geom.size.x,geom.size.y)
					op(g) } )
	}
	
}
