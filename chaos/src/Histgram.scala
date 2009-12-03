import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt,pow}

case class Histgram(imgGeom:Geometry[Int],dataGeom:Geometry[Double])
{
	private val histgram = new Array[Array[Double]](imgGeom.size.x,imgGeom.size.y)
	//
	val canvasTransform=Geometry.transform(imgGeom,dataGeom)
	//
	def update(p:Vector[Double],v:Double) : Double = {
		val ip = canvasTransform(p) map {t=>t.asInstanceOf[Int]}
		if ( !imgGeom.frame.isInside(ip) ) return 0
		//
		update(ip.x , ip.y , v)
	}
	//
	private def update(ix:Int , iy:Int , v:Double) : Double = {
		val hist = ( histgram(ix)(iy) + v )*0.5 
		histgram(ix)(iy) = hist
		hist
	}
	//
	def foreach ( op: (Vector[Int],Double)=>Unit ) : Unit = {
		for(ix<- 0 until imgGeom.size.x ; iy <- 0 until imgGeom.size.y ){
			op((ix,iy),histgram(ix)(iy))
		}
	}
	//
	case class sampling(ip:Vector[Int],samplingDegree:Int)
	{
		def foreach ( op:(Vector[Int],Double)=>Unit ) : Unit = { 
			for(	dx <- -samplingDegree to samplingDegree ;
					dy <- -samplingDegree to samplingDegree ) {
				val dv:Vector[Int] = (dx,dy)  
				val jp:Vector[Int] = ip operate( dv , _+_ )
				if(	imgGeom.frame.isInside(jp) ) {
					op(dv,histgram(jp.x)(jp.y))
				}
			}
		}
	}
	//
	def rendering(samplingDegree:Int)(op:(Vector[Int],Double)=>Unit) : Unit = {
		this.foreach { (ip,_) =>
			// 
			var sumFreq   = 0.0
			var sumRatio  = 0.0
			val maxDistSq:Int = 2*samplingDegree*samplingDegree
			val rand = new java.util.Random
			//
			this.sampling(ip,samplingDegree) foreach { (d,hist)=>
				val distSq:Int = d.x*d.x + d.y*d.y
				val r   :Double = abs(0.5+distSq-maxDistSq).asInstanceOf[Double]/maxDistSq.asInstanceOf[Double]
				val ratio= r + 10*rand.nextDouble
				//println("dist,r,ratio="+(dist,r,ratio))
				//
				sumFreq  += ratio*hist
				sumRatio += ratio
			}
			val avgFreq:Double=	if (sumRatio > 0) sumFreq / sumRatio
								else 0
			
			if( avgFreq>0 ){
				op(ip,avgFreq)
			}
		}
	}
}
