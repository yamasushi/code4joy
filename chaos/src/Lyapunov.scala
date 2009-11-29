// calculate lyapnov characteristic expornent
// 14:22 2009/11/09

import Math.{log,abs,max,min,sqrt} 

object Lyapunov
{
	def calc(sys:ChaosSystem,numIter:Int,pt0:(Double,Double)) : Double = {
		val eps = 1e-7
		def calc( pt0:(Double,Double),pt1:(Double,Double)) : Double = {
			def dist(pp:(Vector[Double],Vector[Double])) : Double = {
				val ( s, e) = pp
				val (sx,sy) = (s.x,s.y)
				val (ex,ey) = (e.x,e.y)
				sqrt( (sx-ex)*(sx-ex) + (sy-ey)*(sy-ey) )
			}
			//
			val seq = ( ( sys.from(pt0) zip sys.from(pt1) ) map dist ) 
			val d0  = seq match {
				case Stream.cons(hd,_)=> max( hd , eps )
				case _ => eps
			}
			val dn = (seq drop numIter) match {
				case Stream.cons(hd,_)=> max( hd , eps )
				case _ => eps
			}
			(log(dn)-log(d0))/(numIter.asInstanceOf[Double])
		}
		//
		val (x0,y0) = pt0
		val pt1 = ( x0 + eps , y0 )
		val pt2 = ( x0 - eps , y0 )
		//
		val pt3 = ( x0       , y0+eps )
		val pt4 = ( x0       , y0-eps )
		//
		val pt5 = ( x0-eps   , y0-eps )
		val pt6 = ( x0+eps   , y0+eps )
		//
		val pt7 = ( x0-eps   , y0+eps )
		val pt8 = ( x0+eps   , y0-eps )
		//
		(0. /: List(
					calc(pt1,pt2) , 
					calc(pt3,pt4) ,
					calc(pt5,pt6) ,
					calc(pt7,pt8) ) ) {
			max(_,_)
		}
	}
}
