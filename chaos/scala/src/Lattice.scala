// Lattice
//  Hexagonal sampler
//
import Math.{sqrt}
import scala.collection.immutable._

trait Lattice
{
	val ratio : Double
	val unit  : Double
	//
	def pos(ip : Vector[Int]) : Vector[Double] = {
		(	ip.x.asInstanceOf[Double] * unit ,
			ip.y.asInstanceOf[Double] * ratio * unit )
	}
	//
	//
	override def toString = "Lattice"+
						"(ratio="+ratio+
						",unit="+unit+")"
}

object Lattice
{
	def apply(n:Int,argUnit:Double) = new Lattice{
		override val unit  = argUnit
		override val ratio = sqrt(n.asInstanceOf[Double])
	}
	//
	def neighbor(ip : Vector[Int]) : Array[Vector[Int]] = {
		Array(	Vector( ip.x + 2 , ip.y     ) ,
				Vector( ip.x + 1 , ip.y - 1 ) ,
				Vector( ip.x - 1 , ip.y - 1 ) ,
				Vector( ip.x - 2 , ip.y     ) ,
				Vector( ip.x - 1 , ip.y + 1 ) ,
				Vector( ip.x + 1 , ip.y + 1 ) )
	}
	//
	val scaler = Matrix[Int]( ( -2 , 3 ),( 1 , 2 ) )
	//
	private var scalerCache = Map[Int,Matrix[Int]]( 0->Matrix((1,0),(0,1)) , 1 -> scaler )
	def nthScaler(n:Int) : Matrix[Int] = {
		if ( n < 0 ) return Matrix((1,0),(0,1))
		if ( scalerCache contains(n) ) {
			scalerCache(n)
		}
		else {
			val l:Matrix[Int] = nthScaler(n-1)
			val r:Matrix[Int] = scaler
			//
			val result = Matrix[Int](
					(l(0)(0) * r(0)(0) +  l(0)(1) * r(1)(0) , l(0)(0) * r(0)(1) +  l(0)(1) * r(1)(1)),
					(l(1)(0) * r(0)(0) +  l(1)(1) * r(1)(0) , l(1)(0) * r(0)(1) +  l(1)(1) * r(1)(1)))
			//
			scalerCache = scalerCache+( n -> result )
			//
			result
		}
	}
	//
	def scaling(n:Int)(ip:Vector[Int]):Vector[Int] = {
		if( n==0 ) ip
		//
		val m = nthScaler(n)
		(	m(0)(0)*ip.x + m(0)(1)*ip.y , 
			m(1)(0)*ip.x + m(1)(1)*ip.y )
	}
	//
	def scan(n:Int,ip:Vector[Int])(op:Vector[Int] => Unit) : Unit = {
		sampling(n,ip)(op,{ _:Seq[Unit] => () } )
	}
	//
	def sampling[T](n:Int,ip:Vector[Int])(getOp:Vector[Int]=>T,accOp:Seq[T]=>T ) : T = {
		if( n<0 ){
			//neighbor(ip) foreach{ p => op(p) }
			return getOp(ip)
		}
		//
		val map = scaling(n) _
		val u = map ( ( 3 , -1 ) )
		val v = map ( ( 0 , -2 ) )
		val w = map ( (-3 , -1 ) )
		//
		var param = new Queue[T]
		var p = ip
		param = param enqueue sampling(n-1,p)(getOp,accOp)
		//
		p = p operate(v , _-_)
		param = param enqueue sampling(n-1,p)(getOp,accOp)
		//
		p = p operate(u , _+_)
		param = param enqueue sampling(n-1,p)(getOp,accOp)
		//
		p = p operate(v , _+_)
		param = param enqueue sampling(n-1,p)(getOp,accOp)
		//
		p = p operate(w , _+_)
		param = param enqueue sampling(n-1,p)(getOp,accOp)
		//
		p = p operate(u , _-_)
		param = param enqueue sampling(n-1,p)(getOp,accOp)
		//
		p = p operate(v , _-_)
		param = param enqueue sampling(n-1,p)(getOp,accOp)
		//
		return accOp(param)
	}
}
