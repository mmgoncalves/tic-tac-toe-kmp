import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greet()
//    private var viewModel = GameUiModel(data: GameDataFactory.shared.create())
    @ObservedObject private(set) var viewModel: ViewModel

	var body: some View {
        GeometryReader { proxy in
            let width = proxy.size.width / 3.0
            let height = proxy.size.height / 3.5
            ZStack {
                Color.white
                    .ignoresSafeArea()
                
                drawColumn(items: ["1", "2", "3"], width, height)
            }
        }
	}
    
    private func drawColumn(items: [String], _ width: CGFloat, _ height: CGFloat) -> some View {
        VStack(spacing: 1.4) {
            ForEach(items, id: \.self) { item in
                drawRow(items: items, width, height)
            }
        }
    }
    
    private func drawRow(items: [String], _ width: CGFloat, _ height: CGFloat) -> some View {
        HStack(spacing: 1.4) {
            ForEach(items, id: \.self) { item in
                drawItem(width, height)
            }
        }
    }
    
    private func drawItem(_ width: CGFloat, _ height: CGFloat) -> some View {
        Button(
            action: {},
            label: {
                Text("")
                    .frame(width: width, height: height)
                    .background(Color.white)
                    .border(/*@START_MENU_TOKEN@*/Color.black/*@END_MENU_TOKEN@*/, width: 0.2)
            }
        )
    }
}
//
//struct ContentView_Previews: PreviewProvider {
//	static var previews: some View {
//		ContentView()
//	}
//}

extension ContentView {
    @MainActor
    class ViewModel: ObservableObject {
        @Published var values: [String] = []
        
        func startObserving() async {
            let emittedValues = Greeting().countToTen()
            //  The for await loop handles the asynchronous sequence of emitted values.
            for await number in emittedValues {
                self.values.append(number)
            }
        }
    }
}
