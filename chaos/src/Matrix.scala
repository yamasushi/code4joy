trait Matrix[T] extends Vector[Vector[T]]

object Matrix
{
	def apply[T](u:Vector[T],v:Vector[T]) : Matrix[T] = new Vector[Vector[T]]{
		def apply(i:Int) = i match {
			case 0 => u
			case 1 => v
		}
	}
	
	def apply[T](u:Vector[T],v:Vector[T],w:Vector[T]) : Matrix[T] = new Vector[Vector[T]]{
		def apply(i:Int) = i match {
			case 0 => u
			case 1 => v
			case 2 => w
		}
	}
	//
	implicit def v2m[T](v : Vector[Vector[T]]) : Matrix[T] = {
		new Matrix[T] {
			def apply(i:Int) = v(i)
		}
	}
}
