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
	def sampling(ip:Vector[Int],samplingDegree:Int) : Double = 
	{
		val l = Lattice(3 , 1.0)
		//
		def getOp(ip:Vector[Int]) : Option[Double] = {
			val p = l.pos(ip)
			val ix = p.x.asInstanceOf[Int]
			val iy = p.y.asInstanceOf[Int]
			if ( ix < 0 ) return None
			if ( iy < 0 ) return None
			if ( ix >= imgGeom.size.x ) return None
			if ( iy >= imgGeom.size.y ) return None
			//
			Some( histgram(ix)(iy) )
		}
		//
		def accOp( ov:Seq[Option[Double]] ) : Option[Double] = {
			val v= (ov filter(_!=None)) map { t => (t: @unchecked) match{
				case Some(x) => x
			} }
			if( v isEmpty ) return None
			return Some( ( (0.0 /: v){_+_} ) / v.length.asInstanceOf[Double] )
		}
		
		Lattice.sampling(samplingDegree,ip)(getOp,accOp) match {
			case None    => 0.0
			case Some(t) => t
		}
	}
	//
	def rendering(samplingDegree:Int)(op:(Vector[Int],Double)=>Unit) : Unit = {
		this.foreach { (ip,_) =>
			// 
			val avgFreq:Double = this.sampling(ip,samplingDegree)
			//
			if( avgFreq>0 ){
				op(ip,avgFreq)
			}
		}
	}
}
