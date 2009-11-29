object Matrix
{
    def apply[T](u:Vector[T],v:Vector[T]) : Vector[Vector[T]] = Vector(u,v)
    def apply[T](u:Vector[T],v:Vector[T],w:Vector[T]) : Vector[Vector[T]] = Vector(u,v,w)
}
