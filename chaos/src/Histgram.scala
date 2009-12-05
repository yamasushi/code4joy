import java.awt.{Graphics2D,Color}
import Math.{abs,min,max,log,sqrt,pow}
import scala.collection.immutable._

case class Histgram(imgGeom:Geometry[Int],dataGeom:Geometry[Double])
{
	private val histgram = new Array[Array[Double]](imgGeom.size.x,imgGeom.size.y)
	val eps = 0.5
	//
	val canvasTransform=Geometry.transform(imgGeom,dataGeom)
	//
	def update(p:Vector[Double],v:Double) : Double = {
		var result = 0.0
		//
		val pt = canvasTransform(p)
		val ip = pt map { _.asInstanceOf[Int] }
		var ips= Set( (ip.x,ip.y) )
		//
		for( i <- 0 to 6 ){
			val q  = pt operate( Vector.trig map {t=>t(i*Math.Pi/3.0)*eps} , { _ + _ } )
			val iq = q map { _.asInstanceOf[Int] }
			ips = ips + ((iq.x,iq.y))
		}
		//
		//println(ips)
		//
		ips foreach { ip =>
			result = max( update(ip,v) , result )
		}
		//
		result
	}
	//
	private def update(ip:(Int,Int) , v:Double) : Double = {
		val (ix,iy) = ip
		if ( ix < 0 ) return 0.0
		if ( iy < 0 ) return 0.0
		if ( ix >= imgGeom.size.x ) return 0.0
		if ( iy >= imgGeom.size.y ) return 0.0
		//
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
