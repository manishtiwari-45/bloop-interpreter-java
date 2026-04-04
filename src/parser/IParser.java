package parser; 
  
import runtime.Instruction; 
import java.util.List; 
  
// Abstraction for the parsing stage of the pipeline. 
// DIP: Interpreter depends on THIS, not on Parser directly. 
// OCP: swap Parser for a different implementation without touching Interpreter. 
public interface IParser { 
      List<Instruction> parse(); 
} 