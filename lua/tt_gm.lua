require "vector"
require "geometry"
require "chaos_renderer"
require "chaos_system"

require "ds_gumowski_mira"

img_width  = 800
img_height = 800
mu      =-0.485
nu      = 1.0
param_a = 0.008
param_b = 0.05

max_iter= 2000

header = "A"
pi  = function(t) return math.abs(t) end
psi = function(t) return math.abs(t) end

meta_phi = function(pi,psi)
	return function(t) return pi(t)/(1+psi(t)) end
end

gm = DSGumowskiMira:new(header,param_a,param_b,mu,nu,pi,psi,meta_phi)
print(gm)
chaos_system = ChaosSystem:new(gm,max_iter)
print(chaos_system)

cr = ChaosRenderer:new(chaos_system,100,5.0)
print(cr)
cr:render(img_width,img_height)

