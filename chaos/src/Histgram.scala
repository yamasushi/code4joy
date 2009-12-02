case class Histgram(imgGeom:Geometry[Int],dataGeom:Geometry[Double])
{
	private val histgram = new Array[Array[Double]](imgGeom.size.x,imgGeom.size.y)
	//
	val canvasTransform=Geometry.transform(imgGeom,dataGeom)
	//
	def update(p:Vector[Double],v:Double) : Double = {
		val ip = canvasTransform(p)
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
	def foreach ( op: (Int,Int,Double)=>Unit ) : Unit = {
		for(ix<- 0 until imgGeom.size.x ; iy <- 0 until imgGeom.size.y ){
			op(ix,iy,histgram(ix)(iy))
		}
	}
	//
	case class sampling(ix:Int,iy:Int,samplingDegree:Int)
	{
		def foreach ( op:(Int,Int,Double)=>Unit ) : Unit = { 
			for(	dx <- -samplingDegree to samplingDegree ;
					dy <- -samplingDegree to samplingDegree ) {
				val jp:Vector[Int] = (ix + dx,iy + dy)
				if(	imgGeom.frame.isInside(jp) ) {
					op(dx,dy,histgram(jp.x)(jp.y))
				}
			}
		}
	}
}
