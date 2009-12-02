trait Vector[T]
{
	def apply(i:Int):T
	lazy val x = apply(0)
	lazy val y = apply(1)
	lazy val z = apply(2)
	//
	def map[S](op:T=>S) : Vector[S] = {
		val src = this
		new Vector[S] {
			def apply(i:Int) = op( src.apply(i) )
		}
	}
	//
	def operate(v:Vector[T],op:(T,T)=>T) : Vector[T] = {
		val left  = this
		val right = v
		//
		new Vector[T]{
			def apply(i:Int) = op( left(i) , right(i) )
		}
	}
	//
	def metric(v:Vector[T],binOp:(T,T)=>T,foldOp:Vector[T]=>T) : T = {
		foldOp( operate(v,binOp) )
	}
	//
}

object Vector
{
	def apply[T](xs:T*)     : Vector[T] = s2v(xs)
	def apply[T,S<%T](v:Vector[S]) : Vector[T] = new Vector[T]{
		def apply(i:Int) = v(i).asInstanceOf[T]
	}
	
	implicit def f2v[T]( v:(Int)=>T ) : Vector[T] = new Vector[T]{
		def apply(i:Int) = v(i)
	}
	
	implicit def s2v[T]( v:Seq[T]) : Vector[T] = new Vector[T]{
		def apply(i:Int) = v(i)
	}
	
	implicit def v2t2[T](v:Vector[T]) : (T,T)   = (v.x , v.y)
	implicit def v2t3[T](v:Vector[T]) : (T,T,T) = (v.x , v.y , v.z)
	
	implicit def t2v[T](v:(T,T)) : Vector[T] = new Vector[T]{
		def apply(i:Int):T = i match {
			case 0 => v._1
			case 1 => v._2
		}
	}
	
	implicit def t2v[T](v:(T,T,T)) : Vector[T] = new Vector[T]{
		def apply(i:Int):T = i match {
			case 0 => v._1
			case 1 => v._2
			case 2 => v._3
		}
	}
	//
}
