require "Vector"

Matrix = {}
Matrix_mt = {__index = Matrix}
function Matrix:new(param_u,param_v)
	local o = {}
	o.u = param_u
	o.v = param_v
	return setmetatable(o,Matrix_mt)
end

function Matrix:transpose()
	return Matrix:new( Vector:new( self.u.x , self.v.x ) , Vector:new( self.u.y , self.v.y ) )
end

Matrix_mt.__unm = function(a)   return Matrix:new( -a.u , -a.v ) end
Matrix_mt.__add = function(a,b) return Matrix:new( a.u+b.u , a.u+b.u ) end
Matrix_mt.__sub = function(a,b) return Matrix:new( a.v-b.v , a.v-b.v ) end
Matrix_mt.__tostring = function(a) return string.format("(%s,%s)", tostring(a.u) , tostring(a.v) ) end

Matrix_mt.__mul = function(a,b)
	local tb = b:transpose()
	return Matrix:new(
					Vector:new( Vector:dot(a.u,tb.u) , Vector:dot(a.u,tb.v) ) ,
					Vector:new( Vector:dot(a.v,tb.u) , Vector:dot(a.v,tb.v) ) )
end

