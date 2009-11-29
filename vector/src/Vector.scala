trait Vector[T]
{
    def apply(i:Int):T
    def x : T = apply(0)
    def y : T = apply(1)
    def z : T = apply(2)
}

object Vector
{
    def apply[T](x:T,y:T)     : Vector[T] = t2v((x,y))
    def apply[T](x:T,y:T,z:T) : Vector[T] = t2v((x,y,z))
    
    implicit def f2v[T]( v:(Int)=>T ) : Vector[T] = new Vector[T]{
        def apply(i:Int) = v(i)
    }
    
    implicit def s2v[T]( v:Seq[T]) : Vector[T] = new Vector[T]{
        def apply(i:Int) = v(i)
    }
    
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
