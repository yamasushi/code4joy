import Math.{log,sin,cos,sqrt,abs}

object Transform
{
	
	def map(m0x:(Double,Double),m1x:(Double,Double))(v:(Double,Double)) : (Double,Double) = {
		val (x,y) = v
		val (m00,m01) = m0x
		val (m10,m11) = m1x
		( m00*x + m01*y , m10*x + m11*y ) 
	}
	
	def offset(off:(Double,Double)) : ((Double,Double))=>(Double,Double) = {
		{(p)=> val (x,y) = p;(x + off._1 , y + off._2 )}
	}
	
	def rotate(theta:Double) : ((Double,Double)) => (Double,Double) = {
		val sinT = sin(theta)
		val cosT = cos(theta)
		map((cosT,-sinT),(sinT,cosT))
	}
	def scale(scale0:Double,scale1:Double) : ((Double,Double)) => (Double,Double) = {
		map((scale0,0),(0,scale1))
	}
}
