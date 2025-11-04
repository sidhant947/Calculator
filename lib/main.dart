import 'package:flutter/material.dart';
import 'package:flutter/services.dart'; // For haptic feedback

void main() {
  runApp(const CalculatorApp());
}

class CalculatorApp extends StatelessWidget {
  const CalculatorApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Calculator',
      theme: ThemeData.light(),
      darkTheme: ThemeData.dark(),
      themeMode: ThemeMode.system,
      home: const CalculatorScreen(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class CalculatorScreen extends StatefulWidget {
  const CalculatorScreen({super.key});

  @override
  State<CalculatorScreen> createState() => _CalculatorScreenState();
}

class _CalculatorScreenState extends State<CalculatorScreen> {
  String _currentInput = '';
  String _previousInput = '';
  String _operation = '';
  double? _result;
  bool _isDarkMode = false;

  void _onButtonPressed(String buttonText) {
    // 🔊 Haptic feedback
    if (buttonText == '=') {
      HapticFeedback.mediumImpact();
    } else if (buttonText == 'AC' || buttonText == '⌫') {
      HapticFeedback.mediumImpact();
    } else {
      HapticFeedback.lightImpact();
    }

    setState(() {
      if (buttonText == 'AC') {
        _currentInput = '';
        _previousInput = '';
        _operation = '';
        _result = null;
      } else if (buttonText == '⌫') {
        if (_currentInput.isNotEmpty) {
          _currentInput = _currentInput.substring(0, _currentInput.length - 1);
        }
      } else if (buttonText == '=') {
        if (_previousInput.isNotEmpty &&
            _operation.isNotEmpty &&
            _currentInput.isNotEmpty) {
          double prevNum = double.tryParse(_previousInput) ?? 0;
          double currentNum = double.tryParse(_currentInput) ?? 0;

          switch (_operation) {
            case '+':
              _result = prevNum + currentNum;
              break;
            case '-':
              _result = prevNum - currentNum;
              break;
            case '×':
              _result = prevNum * currentNum;
              break;
            case '÷':
              if (currentNum != 0) {
                _result = prevNum / currentNum;
              } else {
                _currentInput = 'Error';
                _previousInput = '';
                _operation = '';
                return;
              }
              break;
            case '%':
              _result = prevNum % currentNum;
              break;
          }

          _currentInput =
              _result?.toStringAsFixed(2).replaceFirst(RegExp(r'\.?0*$'), '') ??
              '';
          _previousInput = '';
          _operation = '';
        }
      } else if (['+', '-', '×', '÷', '%'].contains(buttonText)) {
        if (_currentInput.isNotEmpty) {
          if (_previousInput.isNotEmpty && _operation.isNotEmpty) {
            double prevNum = double.tryParse(_previousInput) ?? 0;
            double currentNum = double.tryParse(_currentInput) ?? 0;

            switch (_operation) {
              case '+':
                _result = prevNum + currentNum;
                break;
              case '-':
                _result = prevNum - currentNum;
                break;
              case '×':
                _result = prevNum * currentNum;
                break;
              case '÷':
                if (currentNum != 0) {
                  _result = prevNum / currentNum;
                } else {
                  _currentInput = 'Error';
                  _previousInput = '';
                  _operation = '';
                  return;
                }
                break;
              case '%':
                _result = prevNum % currentNum;
                break;
            }

            _previousInput = _result?.toString() ?? '';
            _currentInput = '';
          } else {
            _previousInput = _currentInput;
            _currentInput = '';
          }
          _operation = buttonText;
        }
      } else if (buttonText == '.') {
        if (!_currentInput.contains('.')) {
          if (_currentInput.isEmpty) {
            _currentInput = '0.';
          } else {
            _currentInput += '.';
          }
        }
      } else {
        if (_currentInput == '0' || _currentInput == 'Error') {
          _currentInput = buttonText;
        } else {
          _currentInput += buttonText;
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final isDark = _isDarkMode;

    final backgroundColor = isDark ? Colors.black : Colors.white;
    final displayBackgroundColor = isDark
        ? Colors.black.withOpacity(0.8)
        : Colors.white.withOpacity(0.9);
    final buttonColor = isDark ? Colors.black : Colors.white;
    final textColor = isDark ? Colors.white : Colors.black;
    final operatorTextColor = isDark ? Colors.orange : Colors.indigo;
    final equalButtonColor = isDark ? Colors.orange : Colors.indigo;
    final clearButtonTextColor = isDark ? Colors.orange : Colors.indigo;
    final numberButtonTextColor = isDark ? Colors.white : Colors.black;

    return Scaffold(
      backgroundColor: backgroundColor,
      appBar: AppBar(
        title: Text('Calculator', style: TextStyle(color: textColor)),
        backgroundColor: Colors.transparent,
        actions: [
          IconButton(
            icon: Icon(isDark ? Icons.wb_sunny : Icons.nightlight_round),
            onPressed: () {
              setState(() {
                _isDarkMode = !_isDarkMode;
              });
            },
          ),
        ],
      ),
      body: SafeArea(
        child: Stack(
          children: [
            Positioned.fill(
              child: Align(
                alignment: Alignment.bottomCenter,
                child: Container(
                  margin: const EdgeInsets.only(bottom: 10),
                  width: double.infinity,
                  decoration: BoxDecoration(
                    color: backgroundColor,
                    borderRadius: BorderRadius.vertical(
                      top: Radius.circular(20),
                    ),
                  ),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Container(
                        width: double.infinity,
                        padding: const EdgeInsets.all(20.0),
                        decoration: BoxDecoration(
                          color: displayBackgroundColor,
                          borderRadius: BorderRadius.circular(15.0),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.grey.withOpacity(0.2),
                              spreadRadius: 2,
                              blurRadius: 5,
                              offset: const Offset(0, 3),
                            ),
                          ],
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.end,
                          children: [
                            Text(
                              _previousInput.isNotEmpty
                                  ? '$_previousInput $_operation'
                                  : '',
                              style: TextStyle(
                                fontSize: 24.0,
                                fontWeight: FontWeight.w400,
                                color: isDark ? Colors.white : Colors.black,
                              ),
                            ),
                            const SizedBox(height: 5.0),
                            Text(
                              _currentInput.isNotEmpty ? _currentInput : '0',
                              style: TextStyle(
                                fontSize: 32.0,
                                fontWeight: FontWeight.bold,
                                color: isDark ? Colors.white : Colors.black,
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 20.0),
                      GridView.count(
                        shrinkWrap: true,
                        physics: const NeverScrollableScrollPhysics(),
                        crossAxisCount: 4,
                        crossAxisSpacing: 10.0,
                        mainAxisSpacing: 10.0,
                        children: [
                          // Row 1
                          _buildAnimatedButton(
                            'AC',
                            () => _onButtonPressed('AC'),
                          ),
                          _buildAnimatedButton(
                            '÷',
                            () => _onButtonPressed('÷'),
                          ),
                          _buildAnimatedButton(
                            '×',
                            () => _onButtonPressed('×'),
                          ),
                          _buildAnimatedButton(
                            '⌫',
                            () => _onButtonPressed('⌫'),
                          ),

                          // Row 2
                          _buildAnimatedButton(
                            '7',
                            () => _onButtonPressed('7'),
                          ),
                          _buildAnimatedButton(
                            '8',
                            () => _onButtonPressed('8'),
                          ),
                          _buildAnimatedButton(
                            '9',
                            () => _onButtonPressed('9'),
                          ),
                          _buildAnimatedButton(
                            '-',
                            () => _onButtonPressed('-'),
                          ),

                          // Row 3
                          _buildAnimatedButton(
                            '4',
                            () => _onButtonPressed('4'),
                          ),
                          _buildAnimatedButton(
                            '5',
                            () => _onButtonPressed('5'),
                          ),
                          _buildAnimatedButton(
                            '6',
                            () => _onButtonPressed('6'),
                          ),
                          _buildAnimatedButton(
                            '+',
                            () => _onButtonPressed('+'),
                          ),

                          // Row 4
                          _buildAnimatedButton(
                            '1',
                            () => _onButtonPressed('1'),
                          ),
                          _buildAnimatedButton(
                            '2',
                            () => _onButtonPressed('2'),
                          ),
                          _buildAnimatedButton(
                            '3',
                            () => _onButtonPressed('3'),
                          ),
                          _buildAnimatedButton(
                            '%',
                            () => _onButtonPressed('%'),
                          ),

                          // Row 5
                          _buildAnimatedButton(
                            '00',
                            () => _onButtonPressed('00'),
                          ),
                          _buildAnimatedButton(
                            '0',
                            () => _onButtonPressed('0'),
                          ),
                          _buildAnimatedButton(
                            '.',
                            () => _onButtonPressed('.'),
                          ),
                          _buildAnimatedButton(
                            '=',
                            () => _onButtonPressed('='),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  // ✅ This wraps your original button in animation + passes real onPressed
  Widget _buildAnimatedButton(String text, VoidCallback onPressed) {
    Color buttonColor, textColor;

    if (text == '=') {
      buttonColor = _isDarkMode ? Colors.orange : Colors.indigo;
      textColor = _isDarkMode ? Colors.black : Colors.white;
    } else if (['+', '-', '×', '÷', '%', '⌫'].contains(text)) {
      buttonColor = _isDarkMode ? Colors.black : Colors.white;
      textColor = _isDarkMode ? Colors.orange : Colors.indigo;
    } else if (text == 'AC') {
      buttonColor = _isDarkMode ? Colors.black : Colors.white;
      textColor = _isDarkMode ? Colors.orange : Colors.indigo;
    } else {
      buttonColor = _isDarkMode ? Colors.black : Colors.white;
      textColor = _isDarkMode ? Colors.white : Colors.black;
    }

    return PressScaleAnimation(
      onPressed: onPressed,
      child: _buildButton(text, buttonColor, textColor, onPressed),
    );
  }

  // 🔁 Your original _buildButton — unchanged!
  Widget _buildButton(
    String text,
    Color buttonColor,
    Color textColor,
    VoidCallback onPressed,
  ) {
    if (text == '=') {
      return Container(
        decoration: BoxDecoration(
          color: _isDarkMode ? Colors.orange : Colors.indigo,
          borderRadius: BorderRadius.vertical(
            top: Radius.zero,
            bottom: Radius.circular(30.0),
          ),
          boxShadow: [
            BoxShadow(
              color: Colors.grey.withOpacity(0.3),
              spreadRadius: 2,
              blurRadius: 5,
              offset: const Offset(0, 3),
            ),
          ],
        ),
        child: ElevatedButton(
          onPressed: onPressed,
          style: ElevatedButton.styleFrom(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.vertical(
                top: Radius.zero,
                bottom: Radius.circular(30.0),
              ),
            ),
            padding: const EdgeInsets.symmetric(vertical: 20.0),
            backgroundColor: _isDarkMode ? Colors.orange : Colors.indigo,
            foregroundColor: _isDarkMode ? Colors.black : Colors.white,
          ),
          child: Text(
            text,
            style: const TextStyle(fontSize: 24.0, fontWeight: FontWeight.bold),
          ),
        ),
      );
    }

    return ElevatedButton(
      onPressed: onPressed,
      style: ElevatedButton.styleFrom(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(30.0),
        ),
        padding: const EdgeInsets.all(20.0),
        backgroundColor: buttonColor,
        foregroundColor: textColor,
        elevation: 5,
        shadowColor: Colors.grey.withOpacity(0.3),
      ),
      child: Text(
        text,
        style: const TextStyle(fontSize: 20.0, fontWeight: FontWeight.bold),
      ),
    );
  }
}

// ✨ Lightweight press animation — no layout change
class PressScaleAnimation extends StatefulWidget {
  final Widget child;
  final VoidCallback onPressed;

  const PressScaleAnimation({
    super.key,
    required this.child,
    required this.onPressed,
  });

  @override
  State<PressScaleAnimation> createState() => _PressScaleAnimationState();
}

class _PressScaleAnimationState extends State<PressScaleAnimation> {
  double _scale = 1.0;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTapDown: (_) => setState(() => _scale = 0.95),
      onTapUp: (_) {
        setState(() => _scale = 1.0);
        widget.onPressed();
      },
      onTapCancel: () => setState(() => _scale = 1.0),
      child: AnimatedScale(
        scale: _scale,
        duration: const Duration(milliseconds: 60),
        curve: Curves.easeOut,
        child: widget.child,
      ),
    );
  }
}
