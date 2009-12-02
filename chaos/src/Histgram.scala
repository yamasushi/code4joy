case class Histgram(imgSize:Vector[Int])
{
	val frame = Frame((0,0),imgSize)
	private val histgram = new Array[Array[Double]](imgSize.x,imgSize.y)
	//
	def apply(ix:Int)(iy:Int) : Double = {
		if ( frame.isInside((ix,iy)) ) histgram(ix)(iy)
		else 0
	}
	//
	def update(ix:Int,iy:Int,v:Double) : Double = {
		val hist = ( histgram(ix)(iy) + v )*0.5 
		histgram(ix)(iy) = hist
		hist
	}
	//
	def foreach ( op: (Int,Int,Double)=>Unit ) : Unit = {
		for(ix<- 0 until imgSize.x ; iy <- 0 until imgSize.y ){
			op(ix,iy,histgram(ix)(iy))
		}
	}
	//
	case class sampling(ix:Int,iy:Int,samplingDegree:Int)
	{
		def foreach ( op:(Int,Int,Double)=>Unit ) : Unit = { 
			for(	dx <- -samplingDegree to samplingDegree ;
					dy <- -samplingDegree to samplingDegree ) {
				val jx = ix + dx
				val jy = iy + dy
				if(	jx >= 0 && jx < imgSize.x && 
					jy >= 0 && jy < imgSize.y ) {
					op(dx,dy,histgram(jx)(jy))
				}
			}
		}
	}
}
