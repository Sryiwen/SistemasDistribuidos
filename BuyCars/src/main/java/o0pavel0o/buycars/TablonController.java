package o0pavel0o.buycars;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TablonController {

	@Autowired
	private AnunciosRepository repository;
    

	@Autowired
	private CategoriaRepository categoriaRepository;
	

	@Autowired
	private CocheRepository cocheRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CompraRepository compraRepository;
	
	
	@Autowired
	private VentaRepository ventaRepository;
	
	
	
	@PostConstruct
	public void init() {
		//Creo los usuarios Maria y Roberto
		Usuario u1 = new Usuario("Maria",0,"cerca de casa","maria@gmail.com","678555999");
		Usuario u2 = new Usuario("Roberto",0,"plaza abrantes","roberto@gmail.com","677455558");
		//Creo 3 coches c1 y c2 pertenecen a Maria, c3 a Roberto
		Coche c1 = new Coche("4422FGU", "BMW","320","diesel","rojo","250cv",100.000,5,25000.00);
		Coche c2 = new Coche("5555FGU", "Audi","A5","diesel","azul","350cv",100.000,5,25000.00);
		Coche c3 = new Coche("4422FFF", "Mercedes","A180","gasolina","negro","150cv",100.000,5,25000.00);
		//Creo las categorias
		Categoria ctg1 = new Categoria("4x4");
		Categoria ctg2 = new Categoria("Deportivo");
		Categoria ctg3 = new Categoria("Berlina");
		
		// Persisto las categorias
		categoriaRepository.save(ctg1);
		categoriaRepository.save(ctg2);
		categoriaRepository.save(ctg3);
		
		//Actualizo las categoria a cada vehiculo creado
		c1.setCategoria(ctg1);
		c2.setCategoria(ctg2);
		c3.setCategoria(ctg3);
		
		
		
		//Persisto los usuarios
		usuarioRepository.save(u1);
		usuarioRepository.save(u2);
		
		
        //Persito los coches de cada usuario
		cocheRepository.save(c1);
		cocheRepository.save(c2);
		cocheRepository.save(c3);
		
		
		//Se crean los anuncios: Maria crea 2 anuncios y Roberto 1
		Anuncio p1 = new Anuncio("Maria", "Quiero vender mi coche BMW 320D", 20000.00);
		Anuncio p2 = new Anuncio("Maria", "Vendo Audi", 10000.00);
		Anuncio p3 = new Anuncio("Roberto", "Vendo Mercedes", 15000.00);
		p1.setUsuario(u1);
		p2.setUsuario(u1);
		p3.setUsuario(u2);
		
		p1.setCoche(c1);
		p2.setCoche(c2);
		p3.setCoche(c3);
		
		
		// Creo las ventas
		
		Venta venta1 = new Venta("6/03/2017", 10.000);
		Venta venta2 = new Venta("8/03/2017", 40.000);
		Venta venta3 = new Venta("10/03/2017", 100.000);
		
		venta1.setUsuario(u1);
		venta2.setUsuario(u1);
		venta3.setUsuario(u2);
		
		venta1.setCoche(c1);
		venta2.setCoche(c2);
		venta3.setCoche(c3);
		
		//Persisto los anuncios p1,p2,p3
		repository.save(p1);
		repository.save(p2);
		repository.save(p3);
		
	
		//Persisto las ventas de los usuarios
		
		ventaRepository.save(venta1);
		ventaRepository.save(venta2);
		ventaRepository.save(venta3);
		
		
		
		// Creo las compras
		Compra compra1 = new Compra("11/03/2017", 10.000);
		compra1.setCoche(c2);
		compra1.setVendedor(u1);
		compra1.setComprador(u2);
		compraRepository.save(compra1);
		
		
			
	}

	
	@RequestMapping("/")
	public String tablon(Model model, Pageable page) {

		model.addAttribute("anuncios", repository.findAll(page));
		model.addAttribute("anunciosCount", repository.count());
		model.addAttribute("usuarios", usuarioRepository.count());
		model.addAttribute("categorias", categoriaRepository.count());
		model.addAttribute("coches", cocheRepository.count());

		return "tablon";
	}

	@RequestMapping("/anuncio/nuevo")
	public String nuevoAnuncio(Model model, Anuncio anuncio, Coche coche) {

		repository.save(anuncio);
		cocheRepository.save(coche);

		return "anuncio_guardado";

	}
	
	
	
	@RequestMapping("/anuncio/{id}")
	public String verAnuncio(Model model, @PathVariable long id) {
		
		Anuncio anuncio = repository.findOne(id);

		model.addAttribute("anuncio", anuncio);

		return "ver_anuncio";
	}
	
	
	@RequestMapping("/categoria/nueva")
	public String nuevaCategoria(Model model, Categoria categoria) {

		categoriaRepository.save(categoria);

		return "categoria_guardada";

	}
	
	
	@RequestMapping("/usuario/nuevo")
	public String nuevoUsuario(Model model, Usuario usuario) {

		usuarioRepository.save(usuario);

		return "usuario_guardado";

	}
	
	
	@RequestMapping("/comprar/{id}")
	public String comprar(Model model, Compra compra, @PathVariable long id) {
		
		Coche coche = cocheRepository.findOne(id);
		Anuncio anuncio = repository.findOne(id);
		
		String fechaCompra = "11/03/2017";
		compra.setCoche(coche);
		compra.setComprador(anuncio.getUsuario());
		compra.setFechaCompra(fechaCompra);
		compra.setPrecio_compra(anuncio.getPrecio());
		compra.setVendedor(anuncio.getUsuario());
		
		compraRepository.save(compra);

		return "compra_guardada";

	}
	
	
	@RequestMapping("/vender/{id}")
	public String vender(Model model, Venta venta, @PathVariable long id) {
		
		Coche coche = cocheRepository.findOne(id);
		Anuncio anuncio = repository.findOne(id);
		
		String fechaVenta = "11/03/2017";
		venta.setCoche(coche);
		venta.setUsuario(anuncio.getUsuario());
		venta.setFechaVenta(fechaVenta);
		venta.setPrecio(anuncio.getPrecio());

		ventaRepository.save(venta);

		return "venta_guardada";

	}
	
	
	/*@RequestMapping("/login")
	public String login(Model model,  @PathVariable long id) {
		id = 2;
		Usuario usuario = usuarioRepository.findOne(id);
		usuario.setNombre(usuario.getNombre());
		usuarioRepository.save(usuario);

		return "confirmar_login";

	}*/
	
	
	
}
