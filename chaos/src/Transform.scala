import Math.{log,sin,cos,sqrt,abs}

object Transform
{
	def map( m:Matrix[Double] )(v:Vector[Double]) : Vector[Double] = {
		m map { u=> u metric(v , _ * _ , { u => u.x + u.y } ) }
	}
	
	def offset(off:Vector[Double]) : (Vector[Double])=>Vector[Double] = {
		{p => p operate(off, _ + _ )}
	}
	
	def rotate(theta:Double) : (Vector[Double]) => Vector[Double] = {
		val sinT = sin(theta)
		val cosT = cos(theta)
		map(Matrix(( cosT , -sinT ),( sinT , cosT )))
	}
	def scale(scale0:Double,scale1:Double) : (Vector[Double]) => Vector[Double] = {
		map(Matrix(( scale0 , 0.0 ),( 0.0 , scale1 )))
	}
}
