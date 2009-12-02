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
	def transform(geom:Geometry[Int],dataGeom:Geometry[Double]):(Vector[Double])=>Vector[Int]={
		//
		val ratio=	if (dataGeom.aspectRatio < geom.aspectRatio)
						geom.size.y.asInstanceOf[Double] / dataGeom.size.y
					else
						geom.size.x.asInstanceOf[Double] / dataGeom.size.x
		
		val offset:Vector[Double]=if( dataGeom.aspectRatio < geom.aspectRatio  ){
			//data     canvas
			// **      *****
			// ** ---> *****
			// **      *****
			(	- (dataGeom.frame.min.x*ratio) + (geom.size.x - (dataGeom.size.x*ratio))*0.5 ,
				- (dataGeom.frame.min.y*ratio) )
		}
		else {
			//data     canvas
			// *****      **
			// ***** ---> **
			// *****      **
			(	- (dataGeom.frame.min.x*ratio) ,
				- (dataGeom.frame.min.y*ratio) + (geom.size.y - (dataGeom.size.y*ratio))*0.5 )
		}
		// println("ratio = "+ratio+" , (offsetX,offsetY)="+(offsetX,offsetY))
		
		{ p =>
			Vector(	( p.x*ratio + offset.x).asInstanceOf[Int], 
					(-p.y*ratio - offset.y + geom.size.y).asInstanceOf[Int] ) }
	}
}
