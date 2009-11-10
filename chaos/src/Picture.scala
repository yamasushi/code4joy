// picure handling class

import java.awt.{Graphics2D}

trait Picture[T]
{
	val imgWidth : Int
	val imgHeight: Int
	
	def startPaint      : T
	def endPaint(env:T) : Unit
	def paint(op : Graphics2D=>Unit) : Unit
	
	val aspectRatio = imgWidth.asInstanceOf[Double]/imgHeight.asInstanceOf[Double]
	def doPaint(op : T => Unit ):Unit={
		val env = startPaint
		op(env)
		endPaint(env)
	}
	
}
