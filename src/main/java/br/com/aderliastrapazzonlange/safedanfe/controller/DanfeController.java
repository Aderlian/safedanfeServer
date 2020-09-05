package br.com.aderliastrapazzonlange.safedanfe.controller;

import java.io.File;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.aderliastrapazzonlange.safedanfe.conf.AppWebConfiguration;
import br.com.aderliastrapazzonlange.safedanfe.models.Danfe;
import br.com.aderliastrapazzonlange.safedanfe.repository.CompanyRepository;
import br.com.aderliastrapazzonlange.safedanfe.repository.DanfeRepository;
import br.com.aderliastrapazzonlange.safedanfe.repository.ProviderRepository;
import br.com.aderliastrapazzonlange.safedanfe.service.XmlServer;

@Controller
@RequestMapping("/danfes")
public class DanfeController {
	
	private DanfeRepository danfeRepository;
	private final ProviderRepository providerRepository;
	private final CompanyRepository companyRepository;

	public DanfeController(ProviderRepository providerRepository, CompanyRepository companyRepository,
			DanfeRepository danfeRepository) {
		this.providerRepository = providerRepository;
		this.companyRepository = companyRepository;
		this.danfeRepository = danfeRepository;
	}

	@GetMapping
	public String danfeHome(Model model) {
		Iterable<Danfe> danfe = danfeRepository.findAll();
		
		model.addAttribute("danfeAll", danfe);
		return "ListDanfe";
	}
	
	@GetMapping("{id}")
	public String searchId(@PathVariable Long id, Model model) {
		Optional<Danfe> danfe = danfeRepository.findById(id);
		model.addAttribute("danfeID", danfe.get());
		return "FormDanfe";
	}
	
	@PostMapping("/download")
	public String saveFileInCompanyDirectory(Long id) {
		Optional<Danfe> opt = danfeRepository.findById(id);
		if(opt.isEmpty()) {
			System.out.println("erro danfe não encontrada...");
			return "redirect:/danfes";
		}
		Danfe danfe = opt.get();
		XmlServer xmlServer = new XmlServer();
		String newPath = danfe.getCompany().getFilePath() + danfe.getProvider().getName() + "-"
				+ danfe.getNumberNFE() + ".xml";
		String filePath = AppWebConfiguration.getFilerootpath()+AppWebConfiguration.getFilepathbkp()+
				danfe.getKeyNFE()+".xml";
		try {
			File file = new File(filePath);
			System.out.println("newPath: " + newPath);
			System.out.println("filePath: "+filePath);
			xmlServer.saveXml(newPath, file);
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			return "redirect:/danfes";
		}
		
		
	}

}
