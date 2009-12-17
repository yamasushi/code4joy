require "vector"

Frame = {}
Frame_mt = {__index = Frame}
function Frame:new(param_min,param_max)
	assert(param_min)
	assert(param_max)
	assert(param_min[1] <= param_max[1] )
	assert(param_min[2] <= param_max[2] )
	local o = {}
	o.min = Vector:new( param_min[1] , param_min[2] )
	o.max = Vector:new( param_max[1] , param_max[2] )
	return setmetatable(o,Frame_mt)
end

Frame.empty = Frame:new(Vector.zero,Vector.zero)

Frame_mt.__tostring = function(a) return string.format("(min=%s,max=%s)", tostring(a.min) , tostring(a.max) ) end

function Frame:is_inside(pt)
	assert(self)
	--print(pt)
	return (	(pt[1] >= self.min.x) and (pt[1] <= self.max.x) and
				(pt[2] >= self.min.y) and (pt[2] <= self.max.y) )
end

function Frame:size()
	assert(self)
	return Vector:new(	self.max:x() - self.min:x() ,
						self.max:y() - self.min:y() )
end

function Frame:inflate( p )
	assert(self)
	--print(p)
	local min_x = (p[1] < self.min:x()) and p[1] or self.min:x()
	local min_y = (p[2] < self.min:y()) and p[2] or self.min:y()
	local max_x = (p[1] > self.max:x()) and p[1] or self.max:x()
	local max_y = (p[2] > self.max:y()) and p[2] or self.max:y()
	return Frame:new(
			Vector:new(min_x,min_y) ,
			Vector:new(max_x,max_y) )
end
