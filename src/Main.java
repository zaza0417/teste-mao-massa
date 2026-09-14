import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final NumberFormat FORMATO_NUM = NumberFormat.getInstance(new Locale("pt", "BR"));

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1645.00");

    static {
        FORMATO_NUM.setMinimumFractionDigits(2);
        FORMATO_NUM.setMaximumFractionDigits(2);
    }

    public static void main(String[] args) {

        List<Funcionario> funcionario = new ArrayList<>();
        funcionario.add(new Funcionario("Maria", LocalDate.of(2000,10,18), new BigDecimal(2009.44), "Operador"));
        funcionario.add(new Funcionario("Joao", LocalDate.of(1990,05,12), new BigDecimal(2284.38), "Operador"));
        funcionario.add(new Funcionario("Caio", LocalDate.of(1961,05,02), new BigDecimal(9836.14), "Coordenador"));
        funcionario.add(new Funcionario("Miguel", LocalDate.of(1988,10,14), new BigDecimal(19119.88), "Diretor"));
        funcionario.add(new Funcionario("Alice", LocalDate.of(1995,01,05), new BigDecimal(2234.68), "Recepcionista"));
        funcionario.add(new Funcionario("Heitor", LocalDate.of(1999,11,19), new BigDecimal(1582.72), "Operador"));
        funcionario.add(new Funcionario("Arthur", LocalDate.of(1993,03,31), new BigDecimal(4071.84), "Contador"));
        funcionario.add(new Funcionario("Laura", LocalDate.of(1994,07,8), new BigDecimal(3017.45), "Gerente"));
        funcionario.add(new Funcionario("Heloisa", LocalDate.of(2003,05,24), new BigDecimal(1606.85), "Eletricista"));
        funcionario.add(new Funcionario("Helena", LocalDate.of(1996,9,02), new BigDecimal(2799.93), "Gerente"));

        funcionario.removeIf(f -> f.getNome().equals("Joao"));

        for(Funcionario f : funcionario){
            BigDecimal novoSalario = f.getSalario()
                    .multiply(new BigDecimal("1.10"))
                    .setScale(2, RoundingMode.HALF_UP);
            f.setSalario(novoSalario);
        }

        System.out.println("=== Lista de funcionarios (Depois do aumento) ===");
        imprimirFuncionarios(funcionario);

        Map<String, List<Funcionario>> porFuncao = funcionario.stream().collect(Collectors.groupingBy(Funcionario::getFuncao));

        System.out.println("\n=== Funcionarios agrupados por função ===");
        for(Map.Entry<String, List<Funcionario>> entry : porFuncao.entrySet()){

            System.out.println("Função: " + entry.getKey());
            imprimirFuncionarios(entry.getValue());
        }

        System.out.println("\n=== Aniversariantes de outubro e dezembro ===");
        List<Funcionario> aniversariantePorMes = funcionario
                .stream()
                .filter(f -> f.getDataNascimento().getMonthValue() == 10 || f.getDataNascimento().getMonthValue() == 12).collect(Collectors.toList());
        imprimirFuncionarios(aniversariantePorMes);

        System.out.println("\n=== Funcionario com maior idade ===");
        Funcionario funMaisVelho = funcionario
                .stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento)).orElse(null);

        if(funMaisVelho != null){
            int idade = Period.between(funMaisVelho.getDataNascimento(), LocalDate.now()).getYears();
            System.out.println("Nome: " + funMaisVelho.getNome() + " | Idade: " + idade + " anos");
        }

        System.out.println("\n=== Funcionarios em ordem alfabetica ===");
        List<Funcionario> ordemAlfabetica = funcionario
                .stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .collect(Collectors.toList());
        imprimirFuncionarios(ordemAlfabetica);

        System.out.println("\n=== Total dos salários ===");
        BigDecimal totalSalarios = funcionario.stream().map(Funcionario::getSalario).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Total: " + FORMATO_NUM.format(totalSalarios));

        System.out.println("\n=== Salarios mínimos por funcionario ===");
        for (Funcionario f : funcionario) {
            BigDecimal quantidadeSalariosMinimos = f.getSalario()
                    .divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + ": " + FORMATO_NUM.format(quantidadeSalariosMinimos)
                    + " salário(s) mínimo(s)");
        }

    }

    private static void imprimirFuncionarios(List<Funcionario> lista) {
        for (Funcionario f : lista) {
            System.out.println(
                    "Nome: " + f.getNome()
                            + " | Data Nascimento: " + f.getDataNascimento().format(FORMATO_DATA)
                            + " | Salário: " + FORMATO_NUM.format(f.getSalario())
                            + " | Função: " + f.getFuncao()
            );
        }
    }
}
