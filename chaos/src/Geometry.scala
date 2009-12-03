trait Geometry[T]
{
	val aspectRatio:Double
	val size:Vector[T]
	val frame:Frame[T]
}

object Geometry
{
	def apply(fr:Frame[Double]) : Geometry[Double] = {
		new Geometry[Double]{
			val frame = fr
			val size  = fr.max operate(fr.min,{ (l,r) => Math.abs(l-r)+0.000001 } )
			val aspectRatio = size.x / size.y
		}
	}
	
	def apply(dim:Vector[Int]) : Geometry[Int] = {
		new Geometry[Int]{
			val frame = Frame[Int]( Vector(0,0) , Vector(dim.x-1,dim.y-1) )
			val size  = dim
			val aspectRatio = size.x.asInstanceOf[Double] / size.y.asInstanceOf[Double]
		}
	}
	
	// geom     : picture geometry
	// dataGeom : target data geometry
	def transform(	imgGeom:Geometry [Int] , 
					dataGeom:Geometry[Double] ):(Vector[Double])=>Vector[Double] = {
		//
		val ratio=	if (dataGeom.aspectRatio < imgGeom.aspectRatio)
						imgGeom.size.y.asInstanceOf[Double] / dataGeom.size.y
					else
						imgGeom.size.x.asInstanceOf[Double] / dataGeom.size.x
		
		val offset:Vector[Double]=if( dataGeom.aspectRatio < imgGeom.aspectRatio  ){
			//data     canvas
			// **      *****
			// ** ---> *****
			// **      *****
			(	- (dataGeom.frame.min.x*ratio) + (imgGeom.size.x - (dataGeom.size.x*ratio))*0.5 ,
				- (dataGeom.frame.min.y*ratio) )
		}
		else {
			//data     canvas
			// *****      **
			// ***** ---> **
			// *****      **
			(	- (dataGeom.frame.min.x*ratio) ,
				- (dataGeom.frame.min.y*ratio) + (imgGeom.size.y - (dataGeom.size.y*ratio))*0.5 )
		}
		// println("ratio = "+ratio+" , (offsetX,offsetY)="+(offsetX,offsetY))
		
		{ p =>
			val q = ( p map {t=>t*ratio} ) operate(offset,_ + _)
			(q.x , imgGeom.size.y - q.y) }
	}
}
