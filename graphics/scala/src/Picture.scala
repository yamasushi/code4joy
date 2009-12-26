// picure handling class

import java.awt.{Graphics2D}

trait Picture[T]
{
	val geom:Geometry[Int]
	//
	def startPaint      : T
	def endPaint(env:T) : Unit
	def paint(op : Graphics2D=>Unit) : Unit
	//
	def doPaint(op : T => Unit ):Unit={
		val env = startPaint
		op(env)
		endPaint(env)
	}
	
}
