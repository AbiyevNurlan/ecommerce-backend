package az.edu.itbrains.ecommerce.helpers;

import az.edu.itbrains.ecommerce.models.Category;
import az.edu.itbrains.ecommerce.models.Color;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final PhotoRepository photoRepository;




    @Override
    public void run(String... args) throws Exception {

//        List<Category> categories = categoryRepository.findAll();
//        if (categories.isEmpty()) {
//            List<Category> categoryList = List.of(
//                    new Category("Kişi Geyimləri", "kisi-geyimleri"),
//                    new Category("Qadın Geyimləri", "qadin-geyimleri"),
//                    new Category("Uşaq Geyimləri", "usaq-geyimleri"),
//                    new Category("Körpə Geyimləri", "korpe-geyimleri"),
//                    new Category("Alt Geyimlər", "alt-geyimleri"),
//                    new Category("Ayaqqabılar", "ayaqqabilar"),
//                    new Category("Aksesuarlar", "aksesuarlar"),
//                    new Category("İç Geyimlər", "ic-geyimleri"),
//                    new Category("Ev Geyimləri", "ev-geyimleri"),
//                    new Category("İdman Geyimləri", "idman-geyimleri"),
//                    new Category("Klassik Geyimlər", "klassik-geyimler"),
//                    new Category("Gündəlik Geyimlər", "gundelik-geyimler"),
//                    new Category("İş Geyimləri", "is-geyimleri"),
//                    new Category("Xarici Geyimlər", "xarici-geyimler"),
//                    new Category("Yay Geyimləri", "yay-geyimleri"),
//                    new Category("Qış Geyimləri", "qis-geyimleri"),
//                    new Category("Gödəkçələr", "godekceler"),
//                    new Category("Köynəklər", "koynekler"),
//                    new Category("Donlar", "donlar"),
//                    new Category("Ətəklər", "etekler"),
//                    new Category("Şalvarlar", "salvarlar"),
//                    new Category("Cins Şalvarlar", "cins-salvarlar"),
//                    new Category("Sviter və Jumperlər", "sviter-ve-jumperler"),
//                    new Category("Krassovkalar", "krassovkalar"),
//                    new Category("Çantalar", "canta"),
//                    new Category("Kəmərlər", "kemerler"),
//                    new Category("Papaq və Şərflər", "papaq-ve-sarflər"),
//                    new Category("Corablar", "corablar"),
//                    new Category("Zərgərlik və Bəzək Əşyaları", "zergərlik-ve-bezek-esyalari")
//
//            );
//
//            categoryRepository.saveAll(categoryList);
//        }
//
//        List<Product> products = productRepository.findAll();
//        if (products.isEmpty()) {
//            Category c1 = categories.get(0);
//            Category c2 = categories.get(1);
//            Category c3 = categories.get(2);
//            Category c4 = categories.get(3);
//            Category c5 = categories.get(4);
//            Category c6 = categories.get(5);
//            Category c7 = categories.get(6);
//            Category c8 = categories.get(7);
//            Category c9 = categories.get(8);
//            Category c10 = categories.get(9);
//
//            List<Product> productList = List.of(
//                    new Product(
//                            "Nike Air Max 270 Kişi Ayaqqabısı",
//                            "Nike Air Max 270 kişi ayaqqabısı gündəlik rahatlıq və müasir dizaynı bir araya gətirir. Ayağın formasına uyğun material, qalın hava kapsullu daban və nəfəs alan üst hissə ilə həm idman, həm də gündəlik istifadə üçün mükəmməl seçimdir. Uzun müddət istifadədə belə rahatlığını itirmir və material keyfiyyəti ilə seçilir.",
//                            "Rahat kişi ayaqqabısı",
//                            "Material: Mesh + Süni dəri; Rəng: Qara; Ölçü: 41-45; Model: Air Max 270",
//                            189.90, 10, "BRC101", c1
//                    ),
//                    new Product(
//                            "Zara Qadın Oversize Plaş",
//                            "Zara qadın oversize plaşı yüngül və hava keçirdən materialdan hazırlanaraq həm yaz, həm də payız mövsümləri üçün ideal seçimdir. Sadə, lakin zərif dizaynı ilə müxtəlif geyimlərlə kombin etmək mümkündür. Suya davamlılığı və rahat kəsimi ilə gündəlik istifadədə yüksək komfort təmin edir.",
//                            "Qadın oversize plaş",
//                            "Material: Poliester; Rəng: Bej; Ölçü: S–XL; Suya davamlı",
//                            129.50, 15, "BRC102", c2
//                    ),
//                    new Product(
//                            "LC Waikiki Uşaq Qış Kürkü",
//                            "LC Waikiki uşaq qış kürkü qalın iç astarı və yüngül üst materialı ilə şaxtalı havalarda maksimum isti tutma qabiliyyəti təqdim edir. Kapüşonlu dizaynı küləyə qarşı müdafiə edir, həmçinin yuyulduqdan sonra formasını itirmir. Gündəlik aktivlik üçün həm rahat, həm də davamlı bir modeldir.",
//                            "Uşaq qış kürkü",
//                            "Material: Parça + Kürk astar; Ölçü: 5–12 yaş; Rəng: Göy",
//                            69.90, 5, "BRC103", c3
//                    ),
//                    new Product(
//                            "Adidas Runfalcon 3.0 Ayaqqabı",
//                            "Adidas Runfalcon 3.0 idman ayaqqabısı yüngüllüyü və yüksək amortizasiyası ilə günlük qaçışlar və məşqlər üçün idealdır. Nəfəsalan tekstil üst qat, gücləndirilmiş daban və sürüşməyə davamlı altlıq istifadəçinin uzun müddət rahat qaçmasına şərait yaradır. Yoruculuğu minimuma endirən quruluşa sahibdir.",
//                            "Adidas idman ayaqqabısı",
//                            "Material: Mesh; Model: Runfalcon 3.0; Ölçü: 40–45; Rəng: Ağ",
//                            159.00, 12, "BRC104", c6
//                    ),
//                    new Product(
//                            "Mango Qadın Midi Don",
//                            "Mango qadın midi donu klassik və rahat dizaynı ilə gündəlik və xüsusi günlər üçün uyğun bir seçimdir. Yumşaq parçası tenlə təmasda zərif hiss yaradır və nəfəs alan quruluşu ilə uzun müddətlik istifadə üçün idealdır. Dabanlı ayaqqabı və ya idman ayaqqabısı ilə kombin etmək mümkündür.",
//                            "Qadın midi don",
//                            "Material: Viskon; Rəng: Qara; Ölçü: XS–L",
//                            89.99, 7, "BRC105", c2
//                    ),
//                    new Product(
//                            "Colin’s Kişi Cins Şalvarı – Slim Fit",
//                            "Colin’s kişi slim fit cins şalvarı yüksək keyfiyyətli denim parçasından hazırlanıb və həm rahat, həm də gündəlik istifadəyə uyğun kəsimə malikdir. Elastiklik payı hərəkət zamanı dartılma hissini azaldır və uzun müddətli istifadə zamanı belə forması pozulmur. Mavi rəng tonu asan kombin imkanı yaradır.",
//                            "Kişi slim fit cins",
//                            "Material: Denim; Rəng: Mavi; Kəsim: Slim Fit",
//                            59.90, 5, "BRC106", c1
//                    ),
//                    new Product(
//                            "Puma Train Logo Kişi T-Shirti",
//                            "Puma Train Logo kişi T-shirt-i idman və gündəlik istifadə üçün uyğundur. Nəfəsalan parça bədən tərini xaricə ötürərək məşq zamanı tərləmənin qarşısını alır. Minimalist dizaynı və yüksək keyfiyyətli tikişləri ilə uzunmüddətli dözümlülük təmin edir. Rahat oturuşu hər bədən tipinə uyğundur.",
//                            "Kişi idman T-shirti",
//                            "Material: 100% pambıq; Rəng: Ağ; Ölçü: M–XXL",
//                            34.90, 10, "BRC107", c6
//                    ),
//                    new Product(
//                            "DeFacto Qadın İp Askılı Yay Donu",
//                            "DeFacto qadın yay donu yüngül və nəfəsalan parçası ilə isti havalarda maksimum rahatlıq yaradır. Parlaq rəngləri və minimal naxışları yay üslubunu dəstəkləyir. Kəmərli dizaynı bel xəttini vurğulayır və zərif görünüş yaradır. Gündəlik istifadə və tətil üçün ideal seçimdir.",
//                            "Qadın yay donu",
//                            "Material: Pambıq + Viskon; Rəng: Sarı; Ölçü: S–L",
//                            39.90, 8, "BRC108", c8
//                    ),
//                    new Product(
//                            "U.S. Polo Assn. Kişi Polo Köynəyi",
//                            "U.S. Polo Assn. kişi polo köynəyi klassik yaka dizaynı və yüksək keyfiyyətli parçası ilə uzun müddət formasını qoruyur. Nəfəsalan material yayda sərinlik təmin edir. Həm gündəlik, həm də iş mühiti üçün uyğun üslub təqdim edir. Tikişləri keyfiyyətlidir və rəng solmasına qarşı dayanıqlıdır.",
//                            "Kişi polo köynəyi",
//                            "Material: 100% pambıq; Rəng: Mavi; Ölçü: L",
//                            54.90, 6, "BRC109", c1
//                    ),
//                    new Product(
//                            "Koton Qadın Kaşmir Sviter",
//                            "Koton qadın kaşmir sviteri soyuq mövsümlər üçün ideal olan yumşaq və isti tutan materialdan hazırlanmışdır. Minimalist dizaynı həm cins şalvar, həm də ətəklərlə mükəmməl uyğunlaşır. Nəfəsalan kaşmir parça dəriyə xoş hiss verir və uzun müddət deformasiya olmadan istifadəyə imkan yaradır.",
//                            "Qadın kaşmir sviteri",
//                            "Material: Kaşmir qarışımı; Rəng: Krem; Ölçü: S–M",
//                            79.00, 10, "BRC110", c7
//                    )
//            );
//
//            productRepository.saveAll(productList);
//        }


    }
}
