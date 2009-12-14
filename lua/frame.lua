require "vector"

Frame = {}
Frame_mt = {__index = Frame}
function Frame:new(param_min,param_max)
	assert(param_min)
	assert(param_max)
	assert(param_min.x < param_max.x )
	assert(param_min.y < param_max.y )
	local o = {}
	o.min = param_min
	o.max = param_max
	return setmetatable(o,Frame_mt)
end
Frame_mt.__tostring = function(a) return string.format("(min=%s,max=%s)", tostring(a.min) , tostring(a.max) ) end

function Frame:is_inside(pt)
	assert(self)
	--print(pt)
	return (	(pt.x >= self.min.x) and (pt.x <= self.max.x) and
				(pt.y >= self.min.y) and (pt.y <= self.max.y) )
end

function Frame:size()
	assert(self)
	return Vector:new(	self.max.x - self.min.x ,
						self.max.y - self.min.y )
end

function Frame:inflate( p )
	assert(self)
	--print(p)
	local min_x = (p.x < self.min.x) and p.x or self.min.x
	local min_y = (p.y < self.min.y) and p.y or self.min.y
	local max_x = (p.x > self.max.x) and p.x or self.max.x
	local max_y = (p.y > self.max.y) and p.y or self.max.y
	return Frame:new(
			Vector:new(min_x,min_y) ,
			Vector:new(max_x,max_y) )
end
