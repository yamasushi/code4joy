import Math.{log,sin,cos,sqrt,abs}

object Transform
{
	def map( m:Vector[Vector[Double]] )(v:Vector[Double]) : Vector[Double] = {
		(m map{ u=> u operate(v , _ * _) } ) map { u => u.x + u.y }
	}
	
	def offset(off:Vector[Double]) : (Vector[Double])=>Vector[Double] = {
		{p => p operate(off, _ + _ )}
	}
	
	def rotate(theta:Double) : (Vector[Double]) => Vector[Double] = {
		val sinT = sin(theta)
		val cosT = cos(theta)
		map(Vector(Vector(cosT,-sinT),Vector(sinT,cosT)))
	}
	def scale(scale0:Double,scale1:Double) : (Vector[Double]) => Vector[Double] = {
		map(Vector(Vector(scale0,0),Vector(0,scale1)))
	}
}
