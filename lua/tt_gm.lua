require "vector"
require "geometry"
require "chaos_renderer"
require "chaos_system"

require "ds_gumowski_mira"

img_width  = 800
img_height = 800
oval_r     = 0.1

max_iter= 2000
num_traj= 100

header = {"_","A"}

pi={}
pi["A"] = math.abs
pi["_"] = function(t) return t*t end

psi={}
psi["A"] = math.abs
psi["_"] = function(t) return t*t end

meta_phi = function(param_pi,param_psi)
	return function(t) return param_pi(t)/(1+param_psi(t)) end
end

params = {}
for i,h in ipairs(header) do
	--print(i,h,pi[h],psi[h])
	table.insert(params,DSGumowskiMira:new(h,0.008,0.05,-0.485,1.0 , pi[h] , psi[h] , meta_phi))
	table.insert(params,DSGumowskiMira:new(h,0.008,0.05, 0    ,1.0 , pi[h] , psi[h] , meta_phi))
end

for i,gm in ipairs(params) do
	print(i,gm)
	chaos_system = ChaosSystem:new(gm,max_iter)
	--print(chaos_system)
	cr = ChaosRenderer:new(chaos_system,num_traj,oval_r)
	--print(cr)
	cr:render(img_width,img_height)
end
