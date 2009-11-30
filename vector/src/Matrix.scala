trait Matrix[T] extends Vector[Vector[T]]

object Matrix
{
	def apply[T](u:Vector[T]*) : Matrix[T] = Vector(u: _*)
	implicit def v2m[T](v : Vector[Vector[T]]) : Matrix[T] = {
		new Matrix[T] {
			def apply(i:Int) = v(i)
		}
	}
}
