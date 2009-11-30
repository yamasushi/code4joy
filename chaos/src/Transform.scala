import Math.{log,sin,cos,sqrt,abs}

object Transform
{
	def map( m:Vector[Vector[Double]] )(v:Vector[Double]) : Vector[Double] = {
		( m.x.x * v.x + m.x.y*v.y , m.y.x * v.x + m.y.y*v.y ) 
	}
	
	def offset(off:Vector[Double]) : (Vector[Double])=>Vector[Double] = {
		{(p)=>(p.x + off.x , p.y + off.y )}
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
